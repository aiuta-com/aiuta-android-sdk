package com.aiuta.fashionsdk.configuration.features.tryon.validation.strings

/**
 * Interface for input image validation text strings.
 *
 * This interface defines the text strings used in the input image validation interface,
 * allowing for customization of validation messages and instructions.
 *
 * The per-reason descriptions correspond to the abort reasons returned by the backend
 * for a try-on operation. An unrecognized reason falls back to [invalidInputImageDescription].
 *
 * @property invalidInputImageDescription Generic message shown when the input image is invalid
 * @property invalidInputImageChangePhotoButton Text for the button to change the invalid photo
 * @property noPeopleDetectedDescription Shown when no person was detected in the photo
 * @property tooManyPeopleDetectedDescription Shown when too many people were detected in the photo
 * @property childDetectedDescription Shown when a minor was detected in the photo
 * @property insufficientTargetAreaDescription Shown when the area to try on is not clearly visible
 * @property internalRestrictionDescription Shown when the result cannot be returned due to internal restrictions
 */
public interface AiutaTryOnInputImageValidationFeatureStrings {
    public val invalidInputImageDescription: String
    public val invalidInputImageChangePhotoButton: String

    public val noPeopleDetectedDescription: String
    public val tooManyPeopleDetectedDescription: String
    public val childDetectedDescription: String
    public val insufficientTargetAreaDescription: String
    public val internalRestrictionDescription: String

    /**
     * Default implementation of [AiutaTryOnInputImageValidationFeatureStrings].
     *
     * Provides standard English text strings for the input image validation interface.
     */
    public class Default : AiutaTryOnInputImageValidationFeatureStrings {
        override val invalidInputImageDescription: String = "We couldn’t detect anyone in this photo"
        override val invalidInputImageChangePhotoButton: String = "Change photo"

        override val noPeopleDetectedDescription: String =
            "We couldn’t detect anyone in this photo. For best results, please upload a well-lit photo of an adult standing straight in front of a pale background."
        override val tooManyPeopleDetectedDescription: String =
            "We detected multiple people in this photo. For best results, please upload a well-lit photo of an adult standing straight in front of a pale background."
        override val childDetectedDescription: String =
            "It looks like this photo might be of a child. For best results, please upload a well-lit photo of an adult standing straight in front of a pale background."
        override val insufficientTargetAreaDescription: String =
            "We couldn’t process this photo because the area to try on isn’t clearly visible. Please upload a photo where it is fully in view."
        override val internalRestrictionDescription: String =
            "We couldn’t process this request because the resulting image didn’t meet our safety and content guidelines. Please try again using a different photo."
    }
}
