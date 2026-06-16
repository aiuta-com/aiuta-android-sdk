package com.aiuta.fashionsdk.tryon.compose.ui.internal.components.images

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaAdaptiveImage
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaPlatformAdaptiveImage
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.LastSavedImageWrapper

@Composable
internal fun ImagesContainer(
    modifier: Modifier = Modifier,
    getImages: () -> List<LastSavedImageWrapper>,
) {
    val images = getImages()

    when (val image = images.firstOrNull()) {
        is LastSavedImageWrapper.SavedPlatformImage -> {
            AiutaPlatformAdaptiveImage(
                modifier = modifier,
                platformFile = image.file,
                contentDescription = null,
            )
        }

        is LastSavedImageWrapper.SavedUrlImage -> {
            AiutaAdaptiveImage(
                modifier = modifier,
                model = image.image.imageUrl,
                contentDescription = null,
            )
        }

        null -> Unit
    }
}
