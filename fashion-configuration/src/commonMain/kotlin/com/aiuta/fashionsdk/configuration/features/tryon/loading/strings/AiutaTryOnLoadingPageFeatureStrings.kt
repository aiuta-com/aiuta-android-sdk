package com.aiuta.fashionsdk.configuration.features.tryon.loading.strings

/**
 * Interface for loading page text strings.
 *
 * This interface defines the text strings used in the loading interface,
 * allowing for customization of user-facing text in the loading UI.
 *
 * @property tryOnLoadingStatusUploadingImage Status text shown while uploading the user's image
 * @property tryOnLoadingStatusScanningBody Status text shown while analyzing the user's body
 * @property tryOnLoadingStatusGeneratingOutfit Status text shown while generating the try-on outfit
 */
public interface AiutaTryOnLoadingPageFeatureStrings {
    public val tryOnLoadingStatusUploadingImage: String
    public val tryOnLoadingStatusScanningBody: String
    public val tryOnLoadingStatusGeneratingOutfit: String

    /**
     * Default implementation of [AiutaTryOnLoadingPageFeatureStrings].
     *
     * Provides standard English text strings for the loading interface.
     */
    public class Default : AiutaTryOnLoadingPageFeatureStrings {
        override val tryOnLoadingStatusUploadingImage: String = "Uploading your photo"
        override val tryOnLoadingStatusScanningBody: String = "Scanning your body"
        override val tryOnLoadingStatusGeneratingOutfit: String = "Generating outfit"
    }
}
