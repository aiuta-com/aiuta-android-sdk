package com.aiuta.fashionsdk.configuration.features.onboarding.bestresult.strings

/**
 * Interface defining text strings used in the best results page of the onboarding flow.
 *
 * This interface provides strings for titles, descriptions, and other text elements
 * used to guide users on how to achieve optimal results.
 */
public interface AiutaOnboardingBestResultsPageFeatureStrings {
    /**
     * Optional title for the best results page, typically showing the current step in the onboarding flow.
     */
    public val onboardingBestResultsPageTitle: String?

    /**
     * Main title for the best results section, emphasizing the importance of following best practices.
     */
    public val onboardingBestResultsTitle: String

    /**
     * Detailed description explaining the requirements for achieving optimal results.
     */
    public val onboardingBestResultsDescription: String

    /**
     * Default implementation of [AiutaOnboardingBestResultsPageFeatureStrings].
     *
     * Provides standard English text for the best results page.
     */
    public class Default : AiutaOnboardingBestResultsPageFeatureStrings {
        override val onboardingBestResultsPageTitle: String = "<b>Step 2/3</b> - For best result"
        override val onboardingBestResultsTitle: String = "For best results"
        override val onboardingBestResultsDescription: String = "Use a photo with good lighting, standing straight against a plain background"
    }
}
