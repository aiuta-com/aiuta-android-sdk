package com.aiuta.fashionsdk.analytics.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(AiutaAnalyticsEvent.EventType.CONFIGURE_EVENT)
public class AiutaAnalyticsConfigureEvent(
    @SerialName("pageId")
    override val pageId: AiutaAnalyticsPageId? = null,
    @SerialName("productId")
    override val productId: String? = null,
    @SerialName("authType")
    public val authenticationType: AuthenticationType,
    @SerialName("consentFeatureType")
    public val сonsentType: ConsentType? = null,
    // General toggles
    @SerialName("welcomeScreenFeatureEnabled")
    public val welcomeScreenFeatureEnabled: Boolean,
    @SerialName("onboardingFeatureEnabled")
    public val onboardingFeatureEnabled: Boolean,
    @SerialName("onboardingBestResultsPageFeatureEnabled")
    public val onboardingBestResultsPageFeatureEnabled: Boolean,
    @SerialName("imagePickerCameraFeatureEnabled")
    public val imagePickerCameraFeatureEnabled: Boolean,
    @SerialName("imagePickerPredefinedModelFeatureEnabled")
    public val imagePickerPredefinedModelFeatureEnabled: Boolean,
    @SerialName("imagePickerUploadsHistoryFeatureEnabled")
    public val imagePickerUploadsHistoryFeatureEnabled: Boolean,
    @SerialName("tryOnFitDisclaimerFeatureEnabled")
    public val tryOnFitDisclaimerFeatureEnabled: Boolean,
    @SerialName("tryOnFeedbackFeatureEnabled")
    public val tryOnFeedbackFeatureEnabled: Boolean,
    @SerialName("tryOnFeedbackOtherFeatureEnabled")
    public val tryOnFeedbackOtherFeatureEnabled: Boolean,
    @SerialName("tryOnGenerationsHistoryFeatureEnabled")
    public val tryOnGenerationsHistoryFeatureEnabled: Boolean,
    @SerialName("tryOnMultiItemFeatureEnabled")
    public val tryOnMultiItemFeatureEnabled: Boolean,
    @SerialName("tryOnWithOtherPhotoFeatureEnabled")
    public val tryOnWithOtherPhotoFeatureEnabled: Boolean,
    @SerialName("shareFeatureEnabled")
    public val shareFeatureEnabled: Boolean,
    @SerialName("shareWatermarkFeatureEnabled")
    public val shareWatermarkFeatureEnabled: Boolean,
    @SerialName("wishlistFeatureEnabled")
    public val wishlistFeatureEnabled: Boolean,
) : AiutaAnalyticsEvent {

    @Serializable
    public enum class AuthenticationType {
        @SerialName("apiKey")
        API_KEY,

        @SerialName("jwt")
        JWT,
    }

    @Serializable
    public enum class ConsentType {
        @SerialName("embeddedIntoOnboarding")
        EMBEDDED_INTO_ONBOARDING,

        @SerialName("standaloneOnboardingPage")
        STANDALONE_ONBOARDING_PAGE,

        @SerialName("standaloneImagePickerPage")
        STANDALONE_IMAGE_PICKER_PAGE,
    }
}
