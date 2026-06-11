package com.aiuta.fashionsdk.configuration.features.onboarding.bestresult.images

import com.aiuta.fashionsdk.compose.resources.media.AiutaMedia

/**
 * Interface defining images used in the best results page of the onboarding flow.
 *
 * This interface provides collections of images for showcasing good and bad
 * examples of app usage in the best results showcase.
 */
public interface AiutaOnboardingBestResultsPageFeatureImages {

    /**
     * Media (image with an optional looping video) shown on the "Best Results"
     * page to illustrate good and bad input examples. When the media has no
     * video source, its image is shown as the content.
     */
    public val onboardingBestResultsItem: AiutaMedia
}
