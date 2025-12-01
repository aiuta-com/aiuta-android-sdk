package com.aiuta.fashionsdk.configuration.ui.theme.image.shapes

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
/**
 * Shape configuration for image components.
 *
 * This abstract class defines the corner radius values used for shaping different
 * sizes of images in the SDK. It provides a consistent visual appearance for
 * rounded corners across various image types and exposes pre-computed shape
 * instances for convenience.
 *
 * @property imageL Corner radius for large-sized images
 * @property imageM Corner radius for medium-sized images
 * @property imageS Corner radius for small-sized images
 * @property imageLShape Pre-computed shape for large-sized images
 * @property imageMShape Pre-computed shape for medium-sized images
 * @property imageSShape Pre-computed shape for small-sized images
 */
public abstract class AiutaImageThemeShapes {
    public abstract val imageL: Dp
    public abstract val imageM: Dp
    public abstract val imageS: Dp

    public val imageLShape: CornerBasedShape by lazy { RoundedCornerShape(imageL) }
    public val imageMShape: CornerBasedShape by lazy { RoundedCornerShape(imageM) }
    public val imageSShape: CornerBasedShape by lazy { RoundedCornerShape(imageS) }

    /**
     * Default implementation of [AiutaImageThemeShapes].
     *
     * This class provides standard corner radius values for images.
     *
     * ```kotlin
     * theme {
     *     image {
     *         shapes = AiutaImageThemeShapes.Default()
     *     }
     * }
     * ```
     */
    public class Default : AiutaImageThemeShapes() {
        override val imageL: Dp = 24.dp
        override val imageM: Dp = 16.dp
        override val imageS: Dp = 8.dp
    }
}
