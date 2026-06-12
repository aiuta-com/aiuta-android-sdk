package com.aiuta.fashionsdk.configuration.mode.shoes.picker.strings

/**
 * Interface defining text strings used in the shoes-mode image picker.
 */
public interface AiutaShoesModeImagePickerStrings {
    /**
     * Text shown in the image picker empty state for shoes mode.
     */
    public val imagePickerShoesDescriptionEmpty: String

    /**
     * Default implementation of [AiutaShoesModeImagePickerStrings].
     *
     * Provides standard English text for the shoes-mode image picker.
     */
    public class Default : AiutaShoesModeImagePickerStrings {
        override val imagePickerShoesDescriptionEmpty: String = "Select a photo where your feet are clearly visible"
    }
}
