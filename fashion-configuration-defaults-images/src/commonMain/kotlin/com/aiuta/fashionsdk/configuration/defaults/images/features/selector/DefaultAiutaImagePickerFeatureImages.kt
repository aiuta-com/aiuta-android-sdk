package com.aiuta.fashionsdk.configuration.defaults.images.features.selector

import com.aiuta.fashion_configuration_defaults_images.generated.resources.Res
import com.aiuta.fashion_configuration_defaults_images.generated.resources.selector_empty_small_image_1
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaComposeDrawableResource
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaDrawableResource
import com.aiuta.fashionsdk.configuration.features.picker.images.AiutaImagePickerFeatureImages

/**
 * Default implementation of [AiutaImagePickerFeatureImages].
 *
 * This class provides the default image resources for the image picker feature,
 * including example images that demonstrate proper image selection for try-on.
 *
 * @property examples List of example images showing both empty state and good examples
 *                   of images suitable for try-on
 */
public class DefaultAiutaImagePickerFeatureImages : AiutaImagePickerFeatureImages {
    override val examples: List<AiutaDrawableResource> = listOf(
        AiutaComposeDrawableResource(Res.drawable.selector_empty_small_image_1),
    )
}
