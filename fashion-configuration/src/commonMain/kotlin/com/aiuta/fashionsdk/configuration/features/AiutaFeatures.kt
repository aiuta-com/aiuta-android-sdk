package com.aiuta.fashionsdk.configuration.features

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.annotations.AiutaDsl
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentEmbeddedIntoOnboardingFeature
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentFeature
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentStandaloneFeature
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentStandaloneImagePickerPageFeature
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentStandaloneOnboardingPageFeature
import com.aiuta.fashionsdk.configuration.features.exceptions.NoSuchFeatureException
import com.aiuta.fashionsdk.configuration.features.onboarding.AiutaOnboardingFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.bestresult.AiutaOnboardingBestResultsPageFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.howworks.AiutaOnboardingHowItWorksPageFeature
import com.aiuta.fashionsdk.configuration.features.picker.AiutaImagePickerFeature
import com.aiuta.fashionsdk.configuration.features.picker.camera.AiutaImagePickerCameraFeature
import com.aiuta.fashionsdk.configuration.features.picker.gallery.AiutaImagePickerPhotoGalleryFeature
import com.aiuta.fashionsdk.configuration.features.picker.history.AiutaImagePickerUploadsHistoryFeature
import com.aiuta.fashionsdk.configuration.features.picker.model.AiutaImagePickerPredefinedModelFeature
import com.aiuta.fashionsdk.configuration.features.picker.protection.AiutaImagePickerProtectionDisclaimerFeature
import com.aiuta.fashionsdk.configuration.features.share.AiutaShareFeature
import com.aiuta.fashionsdk.configuration.features.share.watermark.AiutaShareWatermarkFeature
import com.aiuta.fashionsdk.configuration.features.sizefit.AiutaSizeFitFeature
import com.aiuta.fashionsdk.configuration.features.tryon.AiutaTryOnFeature
import com.aiuta.fashionsdk.configuration.features.tryon.cart.AiutaTryOnCartFeature
import com.aiuta.fashionsdk.configuration.features.tryon.cart.outfit.AiutaTryOnCartOutfitFeature
import com.aiuta.fashionsdk.configuration.features.tryon.disclaimer.AiutaTryOnFitDisclaimerFeature
import com.aiuta.fashionsdk.configuration.features.tryon.feedback.AiutaTryOnFeedbackFeature
import com.aiuta.fashionsdk.configuration.features.tryon.feedback.other.AiutaTryOnFeedbackOtherFeature
import com.aiuta.fashionsdk.configuration.features.tryon.history.AiutaTryOnGenerationsHistoryFeature
import com.aiuta.fashionsdk.configuration.features.tryon.loading.AiutaTryOnLoadingPageFeature
import com.aiuta.fashionsdk.configuration.features.tryon.other.AiutaTryOnWithOtherPhotoFeature
import com.aiuta.fashionsdk.configuration.features.tryon.validation.AiutaTryOnInputImageValidationFeature
import com.aiuta.fashionsdk.configuration.features.welcome.AiutaWelcomeScreenFeature
import com.aiuta.fashionsdk.configuration.features.wishlist.AiutaWishlistFeature
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription

/**
 * Central feature configuration class for the Aiuta SDK.
 *
 * This immutable class manages all feature configurations for the SDK, providing
 * a type-safe way to enable, disable, and configure various SDK features.
 * Features are organized into categories such as onboarding, image picking,
 * try-on functionality, sharing, and more.
 *
 * Required features:
 * - [imagePicker]: Image selection and management
 * - [tryOn]: Core try-on functionality
 *
 * Optional features:
 * - [welcomeScreen]: Welcome screen display
 * - [onboarding]: User onboarding flow
 * - [consent]: User consent management
 * - [share]: Image sharing capabilities
 * - [wishlist]: Wishlist functionality
 * - [sizeFit]: Size recommendation functionality
 *
 * @property welcomeScreen Optional welcome screen feature configuration
 * @property onboarding Optional onboarding flow feature configuration
 * @property consent Optional consent management feature configuration
 * @property imagePicker Required image picker feature configuration
 * @property tryOn Required try-on feature configuration
 * @property share Optional sharing feature configuration
 * @property wishlist Optional wishlist feature configuration
 * @property sizeFit Optional size fit feature configuration
 * @see AiutaFeature
 */
@Immutable
public class AiutaFeatures(
    public val welcomeScreen: AiutaWelcomeScreenFeature?,
    public val onboarding: AiutaOnboardingFeature?,
    public val consent: AiutaConsentFeature?,
    public val imagePicker: AiutaImagePickerFeature,
    public val tryOn: AiutaTryOnFeature,
    public val share: AiutaShareFeature?,
    public val wishlist: AiutaWishlistFeature?,
    public val sizeFit: AiutaSizeFitFeature?,
) {
    /**
     * Provides a feature instance by its type, returning null if not configured.
     *
     * This method allows type-safe access to feature configurations using reified
     * generics. It supports both top-level features and nested sub-features.
     *
     * @param T The type of feature to retrieve
     * @return The feature instance if configured, null otherwise
     * @throws NoSuchFeatureException if the feature type is not recognized
     * @see strictProvideFeature
     * @see isFeatureInitialize
     */
    public inline fun <reified T : AiutaFeature> provideFeature(): T? = when (T::class) {
        // Welcome
        AiutaWelcomeScreenFeature::class -> welcomeScreen
        // Onboarding
        AiutaOnboardingFeature::class -> onboarding
        AiutaOnboardingHowItWorksPageFeature::class -> onboarding?.howItWorksPage
        AiutaOnboardingBestResultsPageFeature::class -> onboarding?.bestResultsPage
        // Consent
        AiutaConsentFeature::class -> consent
        AiutaConsentEmbeddedIntoOnboardingFeature::class -> consent
        AiutaConsentStandaloneImagePickerPageFeature::class -> consent
        AiutaConsentStandaloneOnboardingPageFeature::class -> consent
        AiutaConsentStandaloneFeature::class -> consent
        // Image selector
        AiutaImagePickerFeature::class -> imagePicker
        AiutaImagePickerCameraFeature::class -> imagePicker.camera
        AiutaImagePickerPhotoGalleryFeature::class -> imagePicker.photoGallery
        AiutaImagePickerPredefinedModelFeature::class -> imagePicker.predefinedModels
        AiutaImagePickerProtectionDisclaimerFeature::class -> imagePicker.protectionDisclaimer
        AiutaImagePickerUploadsHistoryFeature::class -> imagePicker.uploadsHistory
        // Try on
        AiutaTryOnFeature::class -> tryOn
        AiutaTryOnLoadingPageFeature::class -> tryOn.loadingPage
        AiutaTryOnInputImageValidationFeature::class -> tryOn.inputImageValidation
        AiutaTryOnCartFeature::class -> tryOn.cart
        AiutaTryOnCartOutfitFeature::class -> tryOn.cart.outfit
        AiutaTryOnFitDisclaimerFeature::class -> tryOn.fitDisclaimer
        AiutaTryOnFeedbackFeature::class -> tryOn.feedback
        AiutaTryOnFeedbackOtherFeature::class -> tryOn.feedback?.otherFeedback
        AiutaTryOnGenerationsHistoryFeature::class -> tryOn.generationsHistory
        AiutaTryOnWithOtherPhotoFeature::class -> tryOn.otherPhoto
        // Share
        AiutaShareFeature::class -> share
        AiutaShareWatermarkFeature::class -> share?.watermark
        // Wishlist
        AiutaWishlistFeature::class -> wishlist
        // Size fit
        AiutaSizeFitFeature::class -> sizeFit
        else -> throw NoSuchFeatureException(T::class.simpleName)
    } as? T

    /**
     * Provides a feature instance by its type, throwing an exception if not configured.
     *
     * This method is similar to [provideFeature] but guarantees a non-null result.
     * Use this when you know a feature should be configured and want to fail fast
     * if it's missing.
     *
     * @param T The type of feature to retrieve
     * @return The feature instance (guaranteed non-null)
     * @throws IllegalStateException if the feature is not configured
     * @throws NoSuchFeatureException if the feature type is not recognized
     * @see provideFeature
     */
    public inline fun <reified T : AiutaFeature> strictProvideFeature(): T = checkNotNull(provideFeature()) {
        """
                Feature ${T::class.simpleName} is not initialized, therefore we can't provide it.
                Please, don't use it or create instance of AiutaFeatures with this feature.
        """.trimIndent()
    }

    /**
     * Checks if a feature is configured and available.
     *
     * This method provides a safe way to check feature availability before
     * attempting to use it, avoiding exceptions.
     *
     * @param T The type of feature to check
     * @return true if the feature is configured, false otherwise
     * @see provideFeature
     */
    public inline fun <reified T : AiutaFeature> isFeatureInitialize(): Boolean = provideFeature<T>() != null

    /**
     * Builder class for creating [AiutaFeatures] instances with DSL-style configuration.
     *
     * This builder ensures required features are set and validates the configuration
     * before creating the final [AiutaFeatures] instance.
     */
    @AiutaDsl
    public class Builder {
        /**
         * Optional welcome screen feature configuration.
         */
        public var welcomeScreen: AiutaWelcomeScreenFeature? = null

        /**
         * Optional onboarding flow feature configuration.
         */
        public var onboarding: AiutaOnboardingFeature? = null

        /**
         * Optional consent management feature configuration.
         */
        public var consent: AiutaConsentFeature? = null

        /**
         * Required image picker feature configuration.
         * Must be set before calling [build].
         */
        public var imagePicker: AiutaImagePickerFeature? = null

        /**
         * Required try-on feature configuration.
         * Must be set before calling [build].
         */
        public var tryOn: AiutaTryOnFeature? = null

        /**
         * Optional sharing feature configuration.
         */
        public var share: AiutaShareFeature? = null

        /**
         * Optional wishlist feature configuration.
         */
        public var wishlist: AiutaWishlistFeature? = null

        /**
         * Optional size fit feature configuration.
         */
        public var sizeFit: AiutaSizeFitFeature? = null

        /**
         * Creates an [AiutaFeatures] instance with the configured properties.
         *
         * @return Configured [AiutaFeatures] instance
         * @throws IllegalArgumentException if required properties are not set
         */
        public fun build(): AiutaFeatures {
            val parentClass = "AiutaFeatures"

            return AiutaFeatures(
                welcomeScreen = welcomeScreen,
                onboarding = onboarding,
                consent = consent,
                imagePicker = imagePicker.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "imagePicker",
                ),
                tryOn = tryOn.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "tryOn",
                ),
                share = share,
                wishlist = wishlist,
                sizeFit = sizeFit,
            )
        }
    }
}

/**
 * Extension function for configuring features within an [AiutaConfiguration.Builder].
 *
 * This extension provides a convenient DSL for configuring features as part of
 * the main configuration setup.
 *
 * @param block Configuration block for setting up features
 * @return The configuration builder for method chaining
 * @see AiutaConfiguration.Builder
 * @see AiutaFeatures.Builder
 */
public inline fun AiutaConfiguration.Builder.features(
    block: AiutaFeatures.Builder.() -> Unit,
): AiutaConfiguration.Builder = apply {
    features = AiutaFeatures.Builder().apply(block).build()
}
