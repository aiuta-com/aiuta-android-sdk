package com.aiuta.fashionsdk.configuration.features.onboarding.howworks.images

import com.aiuta.fashionsdk.compose.resources.media.AiutaMedia

/**
 * Interface defining images used in the "How It Works" page of the onboarding flow.
 *
 * This interface provides a collection of image pairs that demonstrate the app's
 * functionality through before/after examples.
 */
public interface AiutaOnboardingHowItWorksPageFeatureImages {

    /**
     * Media (looping video with a poster image) shown on the "How It Works"
     * page demonstrating the try-on flow. When `null`, no media is shown.
     */
    public val onboardingHowItWorksItem: AiutaMedia
}
