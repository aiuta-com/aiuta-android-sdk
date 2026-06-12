package com.aiuta.fashionsdk.configuration.features.picker.images

import com.aiuta.fashionsdk.compose.resources.media.AiutaMedia

/**
 * Interface defining image resources used in the image picker feature.
 *
 * This interface provides an shoesExample image that can be displayed
 * in the image picker UI, such as a sample photo or suggestion.
 */
public interface AiutaImagePickerFeatureImages {
    /**
     * Drawable resource representing an shoesExample image for the image picker.
     */
    public val example: AiutaMedia
}
