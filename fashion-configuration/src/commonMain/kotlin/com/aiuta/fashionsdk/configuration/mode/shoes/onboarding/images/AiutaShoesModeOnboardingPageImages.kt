package com.aiuta.fashionsdk.configuration.mode.shoes.onboarding.images

import com.aiuta.fashionsdk.compose.resources.media.AiutaMedia

/**
 * Interface defining images used in the shoes-mode onboarding best results page.
 */
public interface AiutaShoesModeOnboardingPageImages {

    /**
     * Media (image with an optional looping video) shown on the shoes-mode
     * "Best Results" page to illustrate good and bad input examples. When the
     * media has no video source, its image is shown as the content.
     */
    public val onboardingShoesBestResultsItem: AiutaMedia
}
