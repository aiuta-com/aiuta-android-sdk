package com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.sku

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.GeneratedImageUIModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.toUiModel
import com.aiuta.fashionsdk.tryon.core.data.datasource.image.models.AiutaFileType
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationStatus
import com.aiuta.fashionsdk.tryon.core.domain.models.meta.AiutaTryOnMetadata

@Immutable
internal sealed interface ProductGenerationOperation {
    val operationId: String

    sealed interface LoadingOperation : ProductGenerationOperation {
        class StartGenerationOperation(
            override val operationId: String,
        ) : LoadingOperation

        class UploadedSourceImageOperation(
            override val operationId: String,
        ) : LoadingOperation

        class GenerationProcessingOperation(
            override val operationId: String,
        ) : LoadingOperation
    }

    class SuccessOperation(
        override val operationId: String,
        val uploadedSourceImageId: String,
        val uploadedSourceImage: String,
        val uploadedSourceImageType: AiutaFileType,
        val generatedImages: List<GeneratedImageUIModel>,
        val metadata: AiutaTryOnMetadata,
    ) : ProductGenerationOperation

    class ErrorOperation(
        override val operationId: String,
        val errorMessage: String? = null,
        val exception: Exception? = null,
    ) : ProductGenerationOperation
}

internal fun ProductGenerationStatus.toOperation(): ProductGenerationOperation = when (this) {
    is ProductGenerationStatus.LoadingGenerationStatus.StartGeneration ->
        ProductGenerationOperation.LoadingOperation.StartGenerationOperation(
            operationId = statusId,
        )

    is ProductGenerationStatus.LoadingGenerationStatus.UploadedSourceImage ->
        ProductGenerationOperation.LoadingOperation.UploadedSourceImageOperation(
            operationId = statusId,
        )

    is ProductGenerationStatus.LoadingGenerationStatus.GenerationProcessing ->
        ProductGenerationOperation.LoadingOperation.GenerationProcessingOperation(
            operationId = statusId,
        )

    is ProductGenerationStatus.ErrorGenerationStatus ->
        ProductGenerationOperation.ErrorOperation(
            operationId = statusId,
            errorMessage = errorMessage,
            exception = exception,
        )

    is ProductGenerationStatus.SuccessGenerationStatus ->
        ProductGenerationOperation.SuccessOperation(
            operationId = statusId,
            uploadedSourceImageId = sourceImageId,
            uploadedSourceImage = sourceImageUrl,
            uploadedSourceImageType = sourceImageType,
            generatedImages = images.map { it.toUiModel() },
            metadata = metadata,
        )
}
