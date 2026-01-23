package com.aiuta.fashionsdk.configuration.defaults.icons.features.sizefit

import com.aiuta.fashion_configuration_defaults_icons.generated.resources.Res
import com.aiuta.fashion_configuration_defaults_icons.generated.resources.ic_female_20
import com.aiuta.fashion_configuration_defaults_icons.generated.resources.ic_male_20
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaComposeDrawableResource
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaIcon
import com.aiuta.fashionsdk.configuration.features.sizefit.icons.AiutaSizeFitFeatureIcons

/**
 * Default implementation of [AiutaSizeFitFeatureIcons].
 *
 * This class provides the default icon resources for the size fit feature.
 *
 * @property male20 20x20 pixel male icon
 * @property female20 20x20 pixel female icon
 */
public class DefaultAiutaSizeFitFeatureIcons : AiutaSizeFitFeatureIcons {
    override val male20: AiutaIcon = AiutaIcon(
        iconResource = AiutaComposeDrawableResource(Res.drawable.ic_male_20),
    )
    override val female20: AiutaIcon = AiutaIcon(
        iconResource = AiutaComposeDrawableResource(Res.drawable.ic_female_20),
    )
}
