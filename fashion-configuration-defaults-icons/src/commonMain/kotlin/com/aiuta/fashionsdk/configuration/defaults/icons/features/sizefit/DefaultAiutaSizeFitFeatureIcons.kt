package com.aiuta.fashionsdk.configuration.defaults.icons.features.sizefit

import com.aiuta.fashion_configuration_defaults_icons.generated.resources.Res
import com.aiuta.fashion_configuration_defaults_icons.generated.resources.ic_sizefit_24
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaComposeDrawableResource
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaIcon
import com.aiuta.fashionsdk.configuration.features.sizefit.icons.AiutaSizeFitFeatureIcons

/**
 * Default implementation of [AiutaSizeFitFeatureIcons].
 *
 * This class provides the default icon resources for the size fit feature.
 *
 * @property sizeFit24 24x24 pixel size fit icon
 */
public class DefaultAiutaSizeFitFeatureIcons : AiutaSizeFitFeatureIcons {
    override val sizeFit24: AiutaIcon = AiutaIcon(
        iconResource = AiutaComposeDrawableResource(Res.drawable.ic_sizefit_24),
    )
}
