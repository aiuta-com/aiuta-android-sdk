package com.aiuta.fashionsdk.tryon.core.domain

import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.internal.analytics.InternalAiutaAnalytic
import com.aiuta.fashionsdk.internal.analytics.internalAiutaAnalytic
import com.aiuta.fashionsdk.logger.AiutaLogger
import com.aiuta.fashionsdk.network.paging.models.PageContainer
import com.aiuta.fashionsdk.network.paging.models.PaginationOffset
import com.aiuta.fashionsdk.tryon.core.AiutaTryOn
import com.aiuta.fashionsdk.tryon.core.data.datasource.image.models.UploadedImage
import com.aiuta.fashionsdk.tryon.core.data.datasource.operation.FashionProductOperationsDataSource
import com.aiuta.fashionsdk.tryon.core.data.datasource.operation.models.CreateProductOperationRequest
import com.aiuta.fashionsdk.tryon.core.data.datasource.operation.productOperationsDataSourceFactory
import com.aiuta.fashionsdk.tryon.core.data.datasource.sku.FashionProductDataSource
import com.aiuta.fashionsdk.tryon.core.data.datasource.sku.productDataSourceFactory
import com.aiuta.fashionsdk.tryon.core.domain.analytic.sendInitTryOnEvent
import com.aiuta.fashionsdk.tryon.core.domain.analytic.sendStartTryOnEvent
import com.aiuta.fashionsdk.tryon.core.domain.analytic.sendTryOnPhotoUploadedEvent
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductCatalog
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationContainer
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationItem
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationPlatformImageContainer
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationStatus
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationUrlContainer
import com.aiuta.fashionsdk.tryon.core.domain.models.generateStatusId
import com.aiuta.fashionsdk.tryon.core.domain.models.meta.AiutaTryOnMetadata
import com.aiuta.fashionsdk.tryon.core.domain.models.policies.AiutaTryOnRetryPolicies
import com.aiuta.fashionsdk.tryon.core.domain.models.policies.DefaultAiutaTryOnRetryPolicies
import com.aiuta.fashionsdk.tryon.core.domain.models.toPublic
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.PingOperationSlice
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.exception.AiutaTryOnExceptionType
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.pingOperationSliceFactory
import com.aiuta.fashionsdk.tryon.core.domain.slice.upload.UploadImageSlice
import com.aiuta.fashionsdk.tryon.core.domain.slice.upload.uploadImageSliceFactory
import com.aiuta.fashionsdk.tryon.core.domain.utils.errorListener
import com.aiuta.fashionsdk.tryon.core.domain.utils.trackSpecificTryOnArea
import com.aiuta.fashionsdk.tryon.core.domain.utils.trackTryOnArea
import com.aiuta.fashionsdk.tryon.core.utils.generateFileName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class AiutaTryOnImpl(
    internal val analytic: InternalAiutaAnalytic,
    internal val retryPolicies: AiutaTryOnRetryPolicies,
    internal val logger: AiutaLogger?,
    private val pingOperationSlice: PingOperationSlice,
    private val uploadImageSlice: UploadImageSlice,
    private val productDataSource: FashionProductDataSource,
    private val productOperationsDataSource: FashionProductOperationsDataSource,
) : AiutaTryOn {
    override suspend fun getProductCatalogs(
        paginationOffset: PaginationOffset?,
        paginationLimit: Int?,
    ): PageContainer<ProductCatalog> {
        val skuCatalogs = productDataSource.getProductCatalogs(
            paginationOffset = paginationOffset,
            paginationLimit,
        )

        return PageContainer(
            result = skuCatalogs.result.map { it.toPublic() },
            beforeKey = skuCatalogs.beforeKey,
            afterKey = skuCatalogs.afterKey,
            errors = skuCatalogs.errors,
        )
    }

    override suspend fun getProductItems(
        catalogName: String,
        paginationOffset: PaginationOffset?,
        paginationLimit: Int?,
    ): PageContainer<ProductGenerationItem> {
        val skuDTOs = productDataSource.getProductItems(
            productCatalogName = catalogName,
            paginationOffset = paginationOffset,
            paginationLimit = paginationLimit,
        )

        return PageContainer(
            result = skuDTOs.result.map { it.toPublic() },
            beforeKey = skuDTOs.beforeKey,
            afterKey = skuDTOs.afterKey,
            errors = skuDTOs.errors,
        )
    }

    override fun startProductGeneration(container: ProductGenerationContainer): Flow<ProductGenerationStatus> = flow {
        analytic.sendInitTryOnEvent(container)
        val statusId = generateStatusId()

        errorListener(statusId = statusId) {
            val metadataBuilder = AiutaTryOnMetadata.Builder()

            // Set loading state with previous image urls
            emit(
                ProductGenerationStatus.LoadingGenerationStatus.StartGeneration(
                    statusId = statusId,
                ),
            )

            // Firstly, upload image on backend
            val uploadedImage = trackTryOnArea(
                typeArea = AiutaTryOnExceptionType.UPLOAD_PHOTO_FAILED,
                container = container,
            ) {
                solveUploadingImage(container)
            }
            // Update time for meta info of generation
            metadataBuilder.setUploadDuration()
            emit(
                ProductGenerationStatus.LoadingGenerationStatus.UploadedSourceImage(
                    statusId = statusId,
                    sourceImageId = uploadedImage.id,
                    sourceImageUrl = uploadedImage.url,
                ),
            )

            // Secondly, create sku generation operation
            val newOperation = trackTryOnArea(
                typeArea = AiutaTryOnExceptionType.START_OPERATION_FAILED,
                container = container,
            ) {
                productOperationsDataSource.createProductOperation(
                    request = CreateProductOperationRequest(
                        skuId = container.productId,
                        uploadedImageId = uploadedImage.id,
                    ),
                )
            }
            analytic.sendStartTryOnEvent(container)

            // Wait for the operation, until it is completed
            emit(
                ProductGenerationStatus.LoadingGenerationStatus.GenerationProcessing(
                    statusId = statusId,
                    sourceImageId = uploadedImage.id,
                    sourceImageUrl = uploadedImage.url,
                ),
            )
            val generations = trackSpecificTryOnArea(
                typeArea = AiutaTryOnExceptionType.OPERATION_FAILED,
                failingTypes = setOf(
                    AiutaTryOnExceptionType.OPERATION_ABORTED_FAILED,
                    AiutaTryOnExceptionType.OPERATION_TIMEOUT_FAILED,
                ),
                container = container,
            ) {
                pingOperationSlice.operationPing(newOperation.operationId)
            }
            // Update time for meta info of generation
            metadataBuilder.setTryOnDuration()

            // Check, that we finish successfully and have results
            trackTryOnArea(
                typeArea = AiutaTryOnExceptionType.OPERATION_EMPTY_RESULTS_FAILED,
                container = container,
            ) { check(generations.isNotEmpty()) }

            // Finally, emit result
            emit(
                ProductGenerationStatus.SuccessGenerationStatus(
                    statusId = statusId,
                    sourceImageId = uploadedImage.id,
                    sourceImageUrl = uploadedImage.url,
                    sourceImageType = uploadedImage.type,
                    images = generations.map { it.toPublic(container) },
                    metadata = metadataBuilder.build(),
                ),
            )
        }
    }

    private suspend fun solveUploadingImage(container: ProductGenerationContainer): UploadedImage = when (container) {
        is ProductGenerationPlatformImageContainer -> {
            uploadImageSlice.uploadImage(
                container = container,
                fileName = generateFileName(),
            ).also {
                analytic.sendTryOnPhotoUploadedEvent(container)
            }
        }

        is ProductGenerationUrlContainer -> {
            UploadedImage(
                id = container.fileId,
                url = container.fileUrl,
                type = container.fileType,
            )
        }
    }

    companion object {
        fun create(aiuta: Aiuta): AiutaTryOn = AiutaTryOnImpl(
            analytic = aiuta.internalAiutaAnalytic,
            pingOperationSlice = aiuta.pingOperationSliceFactory,
            uploadImageSlice = aiuta.uploadImageSliceFactory,
            productDataSource = aiuta.productDataSourceFactory,
            productOperationsDataSource = aiuta.productOperationsDataSourceFactory,
            retryPolicies = DefaultAiutaTryOnRetryPolicies,
            logger = aiuta.logger,
        )
    }
}
