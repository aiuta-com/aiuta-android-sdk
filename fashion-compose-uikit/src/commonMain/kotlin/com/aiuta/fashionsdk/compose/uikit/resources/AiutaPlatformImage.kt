package com.aiuta.fashionsdk.compose.uikit.resources

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import coil3.compose.SubcomposeAsyncImage
import com.aiuta.fashionsdk.compose.core.context.LocalAiutaPlatformContext
import com.aiuta.fashionsdk.compose.uikit.internal.progress.ErrorProgress
import com.aiuta.fashionsdk.io.AiutaPlatformFile
import com.aiuta.fashionsdk.io.readByteArray

/**
 * Represents the loading state of a platform-specific image.
 */
@Immutable
internal sealed interface AiutaPlatformImageLoadingState {
    /**
     * The image is currently loading.
     */
    object Loading : AiutaPlatformImageLoadingState

    /**
     * An error occurred while loading the image.
     */
    object Error : AiutaPlatformImageLoadingState

    /**
     * The image has been successfully loaded.
     *
     * @property byteArray The raw byte array of the loaded image
     */
    class Success(val byteArray: ByteArray) : AiutaPlatformImageLoadingState
}

/**
 * A composable that displays an image from a platform-specific file.
 * This component handles loading states and errors automatically.
 *
 * @param platformFile The platform-specific file containing the image data
 * @param contentDescription The content description for accessibility
 * @param modifier The modifier to be applied to the image
 * @param alignment The alignment of the image within its bounds
 * @param contentScale The scale type of the image
 * @param alpha The alpha value for the image
 * @param colorFilter Optional color filter to be applied to the image
 */
@Composable
public fun AiutaPlatformImage(
    platformFile: AiutaPlatformFile,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
) {
    val imageState = remember {
        mutableStateOf<AiutaPlatformImageLoadingState>(AiutaPlatformImageLoadingState.Loading)
    }
    val context = LocalAiutaPlatformContext.current

    LaunchedEffect(Unit) {
        imageState.value = try {
            AiutaPlatformImageLoadingState.Success(platformFile.readByteArray(context))
        } catch (e: Exception) {
            AiutaPlatformImageLoadingState.Error
        }
    }

    // Need box for more smooth transition
    Box(modifier = modifier) {
        when (val state = imageState.value) {
            is AiutaPlatformImageLoadingState.Loading -> Unit
            is AiutaPlatformImageLoadingState.Error -> ErrorProgress(modifier = modifier)
            is AiutaPlatformImageLoadingState.Success -> {
                SubcomposeAsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = state.byteArray,
                    contentScale = contentScale,
                    alignment = alignment,
                    alpha = alpha,
                    colorFilter = colorFilter,
                    contentDescription = contentDescription,
                )
            }
        }
    }
}
