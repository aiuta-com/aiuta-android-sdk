package com.aiuta.fashionsdk.configuration.features.consent

import com.aiuta.fashionsdk.configuration.features.consent.standalone.data.AiutaConsentStandaloneFeatureData
import com.aiuta.fashionsdk.configuration.features.consent.standalone.dataprovider.AiutaConsentStandaloneFeatureDataProvider
import com.aiuta.fashionsdk.configuration.features.consent.standalone.icons.AiutaConsentStandaloneFeatureIcons
import com.aiuta.fashionsdk.configuration.features.consent.standalone.strings.AiutaConsentStandaloneFeatureStrings
import com.aiuta.fashionsdk.configuration.features.consent.standalone.styles.AiutaConsentStandaloneFeatureStyles

/**
 * Interface for standalone consent feature implementations.
 *
 * This interface defines the components required for standalone consent collection,
 * which can be used in various contexts such as onboarding or image picker pages.
 *
 * Required components:
 * - [strings]: Text content for the consent interface
 * - [data]: Data model for consent information
 * - [dataProvider]: Provider for managing consent data
 * - [icons]: Icon resources for the consent interface
 * - [styles]: Visual styles for the consent interface
 *
 * @property strings Text content for the consent interface
 * @property data Data model for consent information
 * @property dataProvider Provider for managing consent data
 * @property icons Icon resources for the consent interface
 * @property styles Visual styles for the consent interface
 */
public interface AiutaConsentStandaloneFeature : AiutaConsentFeature {
    public val strings: AiutaConsentStandaloneFeatureStrings
    public val data: AiutaConsentStandaloneFeatureData
    public val dataProvider: AiutaConsentStandaloneFeatureDataProvider
    public val icons: AiutaConsentStandaloneFeatureIcons?
    public val styles: AiutaConsentStandaloneFeatureStyles
}
