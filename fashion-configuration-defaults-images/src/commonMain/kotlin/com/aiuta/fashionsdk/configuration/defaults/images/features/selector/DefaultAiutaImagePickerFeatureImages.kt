package com.aiuta.fashionsdk.configuration.defaults.images.features.selector

import com.aiuta.fashion_configuration_defaults_images.generated.resources.Res
import com.aiuta.fashion_configuration_defaults_images.generated.resources.selector_empty_placeholder
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaComposeDrawableResource
import com.aiuta.fashionsdk.compose.resources.media.AiutaMedia
import com.aiuta.fashionsdk.compose.resources.media.AiutaMediaContentScale
import com.aiuta.fashionsdk.configuration.features.picker.images.AiutaImagePickerFeatureImages

/**
 * Default implementation of [AiutaImagePickerFeatureImages].
 *
 * This class provides the default image resource for the image picker feature,
 * including an example image that demonstrates proper image selection for try-on.
 *
 * @property example Example image showing an image suitable for try-on
 */
public class DefaultAiutaImagePickerFeatureImages : AiutaImagePickerFeatureImages {
    override val example: AiutaMedia = AiutaMedia(
        imageResource = AiutaComposeDrawableResource(Res.drawable.selector_empty_placeholder),
        videoSource = null,
        contentScale = AiutaMediaContentScale.FIT,
    )
}
