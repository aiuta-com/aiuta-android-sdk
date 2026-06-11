package com.aiuta.fashionsdk.configuration.mode.shoes.onboarding.strings

/**
 * Interface defining text strings used in the shoes-mode onboarding best results page.
 */
public interface AiutaShoesModeOnboardingPageStrings {
    /**
     * Optional title for the best results page, typically showing the current step in the onboarding flow.
     */
    public val onboardingShoesBestResultsPageTitle: String?

    /**
     * Main title for the best results section, emphasizing the importance of following best practices.
     */
    public val onboardingShoesBestResultsTitle: String

    /**
     * Detailed description explaining the requirements for achieving optimal results.
     */
    public val onboardingShoesBestResultsDescription: String

    /**
     * Default implementation of [AiutaShoesModeOnboardingPageStrings].
     *
     * Provides standard English text for the shoes-mode best results page.
     */
    public class Default : AiutaShoesModeOnboardingPageStrings {
        override val onboardingShoesBestResultsPageTitle: String? = null
        override val onboardingShoesBestResultsTitle: String = "For best results"
        override val onboardingShoesBestResultsDescription: String = "Upload a photo where your feet are clearly visible — any angle works"
    }
}
