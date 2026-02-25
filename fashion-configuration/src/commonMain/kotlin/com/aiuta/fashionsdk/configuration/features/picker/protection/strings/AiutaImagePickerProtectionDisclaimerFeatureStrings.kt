package com.aiuta.fashionsdk.configuration.features.picker.protection.strings

/**
 * Interface defining text strings used in the protection disclaimer feature.
 */
public interface AiutaImagePickerProtectionDisclaimerFeatureStrings {
    /**
     * Text informing the user that their photos are protected and private.
     */
    public val protectionDisclaimer: String

    /**
     * Default implementation of [AiutaImagePickerProtectionDisclaimerFeatureStrings].
     *
     * Provides standard English text for the protection disclaimer.
     */
    public class Default : AiutaImagePickerProtectionDisclaimerFeatureStrings {
        override val protectionDisclaimer: String = "Your photos are protected and visible only to you"
    }
}
