package com.aiuta.fashionsdk.configuration.defaults.features.sizefit

import com.aiuta.fashionsdk.configuration.defaults.icons.features.sizefit.DefaultAiutaSizeFitFeatureIcons
import com.aiuta.fashionsdk.configuration.features.AiutaFeatures
import com.aiuta.fashionsdk.configuration.features.sizefit.sizeFit
import com.aiuta.fashionsdk.configuration.features.sizefit.strings.AiutaSizeFitFeatureStrings
import com.aiuta.fashionsdk.configuration.features.sizefit.styles.AiutaSizeFitFeatureStyles

/**
 * Configures the default size fit feature for the Aiuta SDK.
 *
 * This function sets up the size fit feature with default belly, bra,
 * icons, strings, and styles.
 *
 * @return The updated [AiutaFeatures.Builder] instance.
 */
public fun AiutaFeatures.Builder.defaultSizeFit(
    privacyPolicyUrl: String,
): AiutaFeatures.Builder = sizeFit {
    icons = DefaultAiutaSizeFitFeatureIcons()
    strings = AiutaSizeFitFeatureStrings.Default(
        privacyPolicyUrl = privacyPolicyUrl,
    )
    styles = AiutaSizeFitFeatureStyles.Default()
}
