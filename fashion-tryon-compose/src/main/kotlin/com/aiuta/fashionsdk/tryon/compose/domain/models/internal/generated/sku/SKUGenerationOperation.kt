package com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.sku

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.GeneratedImageUIModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.toUiModel
import com.aiuta.fashionsdk.tryon.core.domain.models.SKUGenerationStatus

@Immutable
internal sealed interface SKUGenerationOperation {
    val sourceImage: String

    class LoadingOperation(
        override val sourceImage: String,
    ) : SKUGenerationOperation

    class SuccessOperation(
        override val sourceImage: String,
        val generatedImages: List<GeneratedImageUIModel>,
    ) : SKUGenerationOperation

    class ErrorOperation(
        override val sourceImage: String,
        public val errorMessage: String? = null,
        public val exception: Exception? = null,
    ) : SKUGenerationOperation
}

internal fun SKUGenerationStatus.toOperation(sourceImage: String): SKUGenerationOperation {
    return when (this) {
        is SKUGenerationStatus.LoadingGenerationStatus ->
            SKUGenerationOperation.LoadingOperation(
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
                sourceImage = sourceImage,
                generatedImages = images.map { it.toUiModel() },
            )
    }
}
