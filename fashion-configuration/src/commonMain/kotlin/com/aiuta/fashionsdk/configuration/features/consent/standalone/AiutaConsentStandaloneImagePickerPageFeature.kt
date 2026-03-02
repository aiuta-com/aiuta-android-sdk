package com.aiuta.fashionsdk.configuration.features.consent

import com.aiuta.fashionsdk.configuration.features.AiutaFeature
import com.aiuta.fashionsdk.configuration.features.consent.standalone.data.AiutaConsentStandaloneFeatureData
import com.aiuta.fashionsdk.configuration.features.consent.standalone.dataprovider.AiutaConsentStandaloneFeatureDataProvider
import com.aiuta.fashionsdk.configuration.features.consent.standalone.icons.AiutaConsentStandaloneFeatureIcons
import com.aiuta.fashionsdk.configuration.features.consent.standalone.strings.AiutaConsentStandaloneFeatureStrings
import com.aiuta.fashionsdk.configuration.features.consent.standalone.styles.AiutaConsentStandaloneFeatureStyles
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription

/**
 * Configuration for standalone consent collection in the image picker page.
 *
 * This feature provides a dedicated page for collecting user consent when selecting images,
 * with full customization options for text, data, icons, and styles.
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
public class AiutaConsentStandaloneImagePickerPageFeature(
    public override val strings: AiutaConsentStandaloneFeatureStrings,
    public override val data: AiutaConsentStandaloneFeatureData,
    public override val dataProvider: AiutaConsentStandaloneFeatureDataProvider,
    public override val icons: AiutaConsentStandaloneFeatureIcons?,
    public override val styles: AiutaConsentStandaloneFeatureStyles,
) : AiutaConsentStandaloneFeature {

    /**
     * Builder for creating [AiutaConsentStandaloneImagePickerPageFeature] instances.
     *
     * This builder ensures all required properties are set before creating the feature instance.
     */
    public class Builder : AiutaFeature.Builder {
        public var strings: AiutaConsentStandaloneFeatureStrings? = null
        public var data: AiutaConsentStandaloneFeatureData? = null
        public var dataProvider: AiutaConsentStandaloneFeatureDataProvider? = null
        public var icons: AiutaConsentStandaloneFeatureIcons? = null
        public var styles: AiutaConsentStandaloneFeatureStyles? = null

        public override fun build(): AiutaConsentStandaloneImagePickerPageFeature {
            val parentClass = "AiutaConsentStandaloneImagePickerPageFeature"

            return AiutaConsentStandaloneImagePickerPageFeature(
                strings = strings.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "strings",
                ),
                data = data.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "data",
                ),
                dataProvider = dataProvider.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "dataProvider",
                ),
                icons = icons,
                styles = styles.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "styles",
                ),
            )
        }
    }
}
