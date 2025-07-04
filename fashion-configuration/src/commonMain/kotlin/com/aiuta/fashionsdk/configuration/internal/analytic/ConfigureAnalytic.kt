package com.aiuta.fashionsdk.configuration.internal.analytic

import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsAuthType
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsConfigureEvent
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsConsentFeatureType
import com.aiuta.fashionsdk.authentication.ApiKeyAuthenticationStrategy
import com.aiuta.fashionsdk.authentication.JWTAuthenticationStrategy
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentEmbeddedIntoOnboardingFeature
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentFeature
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentStandaloneImagePickerPageFeature
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentStandaloneOnboardingPageFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.AiutaOnboardingFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.bestresult.AiutaOnboardingBestResultsPageFeature
import com.aiuta.fashionsdk.configuration.features.picker.camera.AiutaImagePickerCameraFeature
import com.aiuta.fashionsdk.configuration.features.picker.history.AiutaImagePickerUploadsHistoryFeature
import com.aiuta.fashionsdk.configuration.features.picker.model.AiutaImagePickerPredefinedModelFeature
import com.aiuta.fashionsdk.configuration.features.share.AiutaShareFeature
import com.aiuta.fashionsdk.configuration.features.share.watermark.AiutaShareWatermarkFeature
import com.aiuta.fashionsdk.configuration.features.tryon.disclaimer.AiutaTryOnFitDisclaimerFeature
import com.aiuta.fashionsdk.configuration.features.tryon.feedback.AiutaTryOnFeedbackFeature
import com.aiuta.fashionsdk.configuration.features.tryon.feedback.other.AiutaTryOnFeedbackOtherFeature
import com.aiuta.fashionsdk.configuration.features.tryon.history.AiutaTryOnGenerationsHistoryFeature
import com.aiuta.fashionsdk.configuration.features.tryon.other.AiutaTryOnWithOtherPhotoFeature
import com.aiuta.fashionsdk.configuration.features.welcome.AiutaWelcomeScreenFeature
import com.aiuta.fashionsdk.configuration.features.wishlist.AiutaWishlistFeature

internal fun AiutaConfiguration.sendConfigurationEvent() {
    aiutaAnalytic.sendEvent(
        event = AiutaAnalyticsConfigureEvent(
            authType = aiuta.toAuthenticationType(),
            welcomeScreenFeatureEnabled = features.isFeatureInitialize<AiutaWelcomeScreenFeature>(),
            onboardingFeatureEnabled = features.isFeatureInitialize<AiutaOnboardingFeature>(),
            onboardingBestResultsPageFeatureEnabled = features.isFeatureInitialize<AiutaOnboardingBestResultsPageFeature>(),
            consentFeatureType = features.provideFeature<AiutaConsentFeature>()?.toConsentType(),
            imagePickerCameraFeatureEnabled = features.isFeatureInitialize<AiutaImagePickerCameraFeature>(),
            imagePickerPredefinedModelFeatureEnabled = features.isFeatureInitialize<AiutaImagePickerPredefinedModelFeature>(),
            imagePickerUploadsHistoryFeatureEnabled = features.isFeatureInitialize<AiutaImagePickerUploadsHistoryFeature>(),
            tryOnFitDisclaimerFeatureEnabled = features.isFeatureInitialize<AiutaTryOnFitDisclaimerFeature>(),
            tryOnFeedbackFeatureEnabled = features.isFeatureInitialize<AiutaTryOnFeedbackFeature>(),
            tryOnFeedbackOtherFeatureEnabled = features.isFeatureInitialize<AiutaTryOnFeedbackOtherFeature>(),
            tryOnGenerationsHistoryFeatureEnabled = features.isFeatureInitialize<AiutaTryOnGenerationsHistoryFeature>(),
            tryOnMultiItemFeatureEnabled = false,
            tryOnWithOtherPhotoFeatureEnabled = features.isFeatureInitialize<AiutaTryOnWithOtherPhotoFeature>(),
            shareFeatureEnabled = features.isFeatureInitialize<AiutaShareFeature>(),
            shareWatermarkFeatureEnabled = features.isFeatureInitialize<AiutaShareWatermarkFeature>(),
            wishlistFeatureEnabled = features.isFeatureInitialize<AiutaWishlistFeature>(),
        ),
    )
}

private fun Aiuta.toAuthenticationType(): AiutaAnalyticsAuthType = when (authenticationStrategy) {
    is ApiKeyAuthenticationStrategy -> AiutaAnalyticsAuthType.API_KEY
    is JWTAuthenticationStrategy -> AiutaAnalyticsAuthType.JWT
}

private fun AiutaConsentFeature.toConsentType(): AiutaAnalyticsConsentFeatureType = when (this) {
    is AiutaConsentEmbeddedIntoOnboardingFeature -> AiutaAnalyticsConsentFeatureType.EMBEDDED_INTO_ONBOARDING
    is AiutaConsentStandaloneOnboardingPageFeature -> AiutaAnalyticsConsentFeatureType.STANDALONE_ONBOARDING_PAGE
    is AiutaConsentStandaloneImagePickerPageFeature -> AiutaAnalyticsConsentFeatureType.STANDALONE_IMAGE_PICKER_PAGE
    else -> error("Not supported consent type")
}
