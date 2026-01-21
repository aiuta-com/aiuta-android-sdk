package com.aiuta.fashionsdk.tryon.compose.ui.internal.components.images

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaPlatformImage
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.LastSavedImageWrapper

@Composable
internal fun ImagesContainer(
    modifier: Modifier = Modifier,
    getImages: () -> List<LastSavedImageWrapper>,
) {
    val theme = LocalTheme.current

    val images = getImages()

    when (val image = images.firstOrNull()) {
        is LastSavedImageWrapper.SavedPlatformImage -> {
            AiutaPlatformImage(
                modifier = modifier,
                platformFile = image.file,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }

        is LastSavedImageWrapper.SavedUrlImage -> {
            AiutaImage(
                modifier = modifier,
                imageUrl = image.image.imageUrl,
                shape = theme.image.shapes.imageLShape,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }

        null -> Unit
    }
}
