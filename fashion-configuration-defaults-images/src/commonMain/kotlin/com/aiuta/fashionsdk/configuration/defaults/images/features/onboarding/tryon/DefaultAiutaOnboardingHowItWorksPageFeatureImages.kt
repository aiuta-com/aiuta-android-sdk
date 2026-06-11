package com.aiuta.fashionsdk.configuration.defaults.images.features.onboarding.tryon

import com.aiuta.fashion_configuration_defaults_images.generated.resources.Res
import com.aiuta.fashion_configuration_defaults_images.generated.resources.onboarding_how_works_cover
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaComposeDrawableResource
import com.aiuta.fashionsdk.compose.resources.media.AiutaMedia
import com.aiuta.fashionsdk.compose.resources.media.AiutaMediaContentScale
import com.aiuta.fashionsdk.configuration.features.onboarding.howworks.images.AiutaOnboardingHowItWorksPageFeatureImages

/**
 * Default implementation of [AiutaOnboardingHowItWorksPageFeatureImages].
 *
 * Provides the default media for the onboarding "How It Works" page: a bundled,
 * looping video that demonstrates the try-on flow, with a poster image shown
 * while it buffers or if playback is unavailable.
 *
 * @property onboardingHowItWorksItem Default [AiutaMedia] — the bundled "how it works"
 *   video paired with a cover image, scaled to fill the surface.
 */
public class DefaultAiutaOnboardingHowItWorksPageFeatureImages : AiutaOnboardingHowItWorksPageFeatureImages {

    override val onboardingHowItWorksItem: AiutaMedia = AiutaMedia(
        imageResource = AiutaComposeDrawableResource(Res.drawable.onboarding_how_works_cover),
        videoSource = null,
        contentScale = AiutaMediaContentScale.FILL,
    )
}
