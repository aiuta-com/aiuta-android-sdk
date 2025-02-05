package com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.sku

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.GeneratedImageUIModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.toUiModel
import com.aiuta.fashionsdk.tryon.core.domain.models.SKUGenerationStatus
import com.aiuta.fashionsdk.tryon.core.domain.models.meta.AiutaTryOnMetadata

@Immutable
internal sealed interface SKUGenerationOperation {
    val sourceImage: String

    sealed interface LoadingOperation : SKUGenerationOperation {
        class StartGenerationOperation(
            override val sourceImage: String,
        ) : LoadingOperation

        class UploadedSourceImageOperation(
            override val sourceImage: String,
        ) : LoadingOperation

        class GenerationProcessingOperation(
            override val sourceImage: String,
        ) : LoadingOperation
    }

    class SuccessOperation(
        val sourceImageId: String,
        override val sourceImage: String,
        val generatedImages: List<GeneratedImageUIModel>,
        val metadata: AiutaTryOnMetadata,
    ) : SKUGenerationOperation

    class ErrorOperation(
        override val sourceImage: String,
        val errorMessage: String? = null,
        val exception: Exception? = null,
    ) : SKUGenerationOperation
}

internal fun SKUGenerationStatus.toOperation(sourceImage: String): SKUGenerationOperation {
    return when (this) {
        is SKUGenerationStatus.LoadingGenerationStatus.StartGeneration ->
            SKUGenerationOperation.LoadingOperation.StartGenerationOperation(
                sourceImage = sourceImage,
            )

        is SKUGenerationStatus.LoadingGenerationStatus.UploadedSourceImage ->
            SKUGenerationOperation.LoadingOperation.UploadedSourceImageOperation(
                sourceImage = sourceImage,
            )

        is SKUGenerationStatus.LoadingGenerationStatus.GenerationProcessing ->
            SKUGenerationOperation.LoadingOperation.GenerationProcessingOperation(
                sourceImage = sourceImage,
            )

        is SKUGenerationStatus.ErrorGenerationStatus ->
            SKUGenerationOperation.ErrorOperation(
                sourceImage = sourceImage,
                errorMessage = errorMessage,
                exception = exception,
            )

        is SKUGenerationStatus.SuccessGenerationStatus ->
            SKUGenerationOperation.SuccessOperation(
                sourceImageId = sourceImageId,
                sourceImage = sourceImage,
                generatedImages = images.map { it.toUiModel() },
                metadata = metadata,
            )
    }
}
