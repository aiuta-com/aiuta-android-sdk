package com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.generated.operations

import androidx.paging.PagingData
import com.aiuta.fashionsdk.configuration.features.models.images.AiutaInputImage
import com.aiuta.fashionsdk.configuration.features.picker.history.dataprovider.AiutaImagePickerUploadsHistoryFeatureDataProviderCustom
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.toPublicHistory
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.operations.GeneratedOperationUIModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.operations.toOperationUiModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.operations.toPublic
import com.aiuta.fashionsdk.tryon.core.data.datasource.image.models.AiutaFileType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class HostGeneratedOperationInteractor(
    private val dataProvider: AiutaImagePickerUploadsHistoryFeatureDataProviderCustom,
) : GeneratedOperationInteractor {
    override fun getGeneratedOperationFlow(): Flow<PagingData<GeneratedOperationUIModel>> = dataProvider.uploadedImages
        .map { images ->
            images.map { image -> image.toOperationUiModel() }
        }
        .map { images -> PagingData.from(images) }

    override suspend fun getFirstGeneratedOperation(): GeneratedOperationUIModel? {
        val firstImage = dataProvider.uploadedImages.value.firstOrNull()
        return firstImage?.let { firstImage.toOperationUiModel() }
    }

    override suspend fun createOperation(imageId: String): String = imageId

    override suspend fun deleteOperation(operation: GeneratedOperationUIModel): Result<Unit> = runCatching {
        dataProvider.deleteUploadedImages(images = operation.toPublic())
    }

    override suspend fun deleteOperations(operations: List<GeneratedOperationUIModel>): Result<Unit> = runCatching {
        dataProvider.deleteUploadedImages(
            images = operations.map { it.toPublic() }.flatten(),
        )
    }

    override fun countGeneratedOperation(): Flow<Int> = dataProvider.uploadedImages.map { images -> images.size }

    override suspend fun createImage(
        sourceImageId: String,
        sourceImageUrl: String,
        sourceImageType: AiutaFileType,
        operationId: String,
    ): Result<Unit> = runCatching {
        dataProvider.addUploadedImages(
            images = listOf(
                AiutaInputImage(
                    id = sourceImageId,
                    url = sourceImageUrl,
                    ownerType = sourceImageType.toPublicHistory(),
                ),
            ),
        )
    }

    companion object {
        fun getInstance(
            dataProvider: AiutaImagePickerUploadsHistoryFeatureDataProviderCustom,
        ): HostGeneratedOperationInteractor = HostGeneratedOperationInteractor(dataProvider = dataProvider)
    }
}
