package com.aiuta.fashionsdk.compose.uikit.resources

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
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
 * Displays an image from a platform-specific file.
 *
 * Reads the file's bytes (showing [ErrorProgress] if that fails) and renders them through
 * [AiutaAdaptiveImage], so platform files get the same adaptive cover / contain+blur treatment and
 * view-sized decoding as URL images.
 *
 * @param platformFile The platform-specific file containing the image data
 * @param contentDescription The content description for accessibility
 * @param modifier The modifier to be applied to the container
 * @param shapeDp The corner radius used to clip the image (and the shimmer placeholder)
 * @param alignment The alignment of the image within its bounds
 */
@Composable
public fun AiutaPlatformAdaptiveImage(
    platformFile: AiutaPlatformFile,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shapeDp: Dp? = null,
    alignment: Alignment = Alignment.Center,
) {
    val context = LocalAiutaPlatformContext.current
    var imageState by remember {
        mutableStateOf<AiutaPlatformImageLoadingState>(AiutaPlatformImageLoadingState.Loading)
    }

    LaunchedEffect(platformFile) {
        imageState = try {
            AiutaPlatformImageLoadingState.Success(platformFile.readByteArray(context))
        } catch (e: Exception) {
            AiutaPlatformImageLoadingState.Error
        }
    }

    // Need box for more smooth transition
    Box(modifier = modifier) {
        when (val state = imageState) {
            is AiutaPlatformImageLoadingState.Loading -> Unit
            is AiutaPlatformImageLoadingState.Error -> ErrorProgress(modifier = Modifier.fillMaxSize())
            is AiutaPlatformImageLoadingState.Success -> {
                AiutaAdaptiveImage(
                    model = state.byteArray,
                    contentDescription = contentDescription,
                    shapeDp = shapeDp,
                    modifier = Modifier.fillMaxSize(),
                    alignment = alignment,
                )
            }
        }
    }
}
