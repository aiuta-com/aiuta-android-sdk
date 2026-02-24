package com.aiuta.fashionsdk.configuration.features.picker.protection

import com.aiuta.fashionsdk.configuration.features.AiutaFeature
import com.aiuta.fashionsdk.configuration.features.picker.AiutaImagePickerFeature
import com.aiuta.fashionsdk.configuration.features.picker.protection.icons.AiutaImagePickerProtectionDisclaimerFeatureIcons
import com.aiuta.fashionsdk.configuration.features.picker.protection.strings.AiutaImagePickerProtectionDisclaimerFeatureStrings
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription

/**
 * Configuration for the protection disclaimer feature in the image picker.
 *
 * This feature displays a privacy notice informing users that their photos
 * are protected and only visible to them.
 *
 * Required components:
 * - [icons]: Icons used in the disclaimer UI
 * - [strings]: Text strings for the disclaimer
 */
public class AiutaImagePickerProtectionDisclaimerFeature(
    public val icons: AiutaImagePickerProtectionDisclaimerFeatureIcons,
    public val strings: AiutaImagePickerProtectionDisclaimerFeatureStrings,
) : AiutaFeature {

    /**
     * Builder class for creating [AiutaImagePickerProtectionDisclaimerFeature] instances.
     */
    public class Builder : AiutaFeature.Builder {
        public var icons: AiutaImagePickerProtectionDisclaimerFeatureIcons? = null
        public var strings: AiutaImagePickerProtectionDisclaimerFeatureStrings? = null

        public override fun build(): AiutaImagePickerProtectionDisclaimerFeature {
            val parentClass = "AiutaImagePickerProtectionDisclaimerFeature"

            return AiutaImagePickerProtectionDisclaimerFeature(
                icons = icons.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "icons",
                ),
                strings = strings.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "strings",
                ),
            )
        }
    }
}

/**
 * DSL function for configuring the protection disclaimer feature.
 *
 * Example usage:
 * ```
 * imagePicker {
 *     protectionDisclaimer {
 *         icons = ...
 *         strings = ...
 *     }
 * }
 * ```
 */
public inline fun AiutaImagePickerFeature.Builder.protectionDisclaimer(
    block: AiutaImagePickerProtectionDisclaimerFeature.Builder.() -> Unit,
): AiutaImagePickerFeature.Builder = apply {
    protectionDisclaimer = AiutaImagePickerProtectionDisclaimerFeature.Builder().apply(block).build()
}
