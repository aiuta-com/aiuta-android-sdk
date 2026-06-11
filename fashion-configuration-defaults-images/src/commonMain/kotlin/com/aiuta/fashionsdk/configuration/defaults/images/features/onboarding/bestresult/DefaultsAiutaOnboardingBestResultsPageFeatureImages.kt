package com.aiuta.fashionsdk.configuration.defaults.images.features.onboarding.bestresult

import com.aiuta.fashion_configuration_defaults_images.generated.resources.Res
import com.aiuta.fashion_configuration_defaults_images.generated.resources.onboarding_best_results_cover
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaComposeDrawableResource
import com.aiuta.fashionsdk.compose.resources.media.AiutaMedia
import com.aiuta.fashionsdk.compose.resources.media.AiutaMediaContentScale
import com.aiuta.fashionsdk.configuration.features.onboarding.bestresult.images.AiutaOnboardingBestResultsPageFeatureImages

/**
 * Default implementation of [AiutaOnboardingBestResultsPageFeatureImages].
 *
 * Provides the default media for the onboarding "Best Results" page — a cover
 * image illustrating good and bad input examples that help users understand what
 * makes for a successful try-on.
 *
 * @property onboardingBestResultsItem Default [AiutaMedia] — the bundled "best results"
 *   cover image (no video), scaled to fit the surface.
 */
public class DefaultsAiutaOnboardingBestResultsPageFeatureImages : AiutaOnboardingBestResultsPageFeatureImages {
    override val onboardingBestResultsItem: AiutaMedia = AiutaMedia(
        imageResource = AiutaComposeDrawableResource(Res.drawable.onboarding_best_results_cover),
        videoSource = null,
        contentScale = AiutaMediaContentScale.FIT,
    )
}
