package com.aiuta.fashionsdk.configuration.features.onboarding.howworks.strings

/**
 * Interface defining text strings used in the "How It Works" page of the onboarding flow.
 *
 * This interface provides strings for titles, descriptions, and other text elements
 * used to explain the app's functionality to new users.
 */
public interface AiutaOnboardingHowItWorksPageFeatureStrings {
    /**
     * Optional title for the "How It Works" page, typically showing the current step in the onboarding flow.
     */
    public val onboardingHowItWorksPageTitle: String?

    /**
     * Main title for the "How It Works" section, highlighting the app's core functionality.
     */
    public val onboardingHowItWorksTitle: String

    /**
     * Brief description explaining how to use the app's main feature.
     */
    public val onboardingHowItWorksDescription: String

    /**
     * Default implementation of [AiutaOnboardingHowItWorksPageFeatureStrings].
     *
     * Provides standard English text for the "How It Works" page.
     */
    public class Default : AiutaOnboardingHowItWorksPageFeatureStrings {
        override val onboardingHowItWorksPageTitle: String = "<b>Step 1/3</b> - How it works"
        override val onboardingHowItWorksTitle: String = "Virtual try-on"
        override val onboardingHowItWorksDescription: String = "Upload a photo and see how items look on you"
    }
}
