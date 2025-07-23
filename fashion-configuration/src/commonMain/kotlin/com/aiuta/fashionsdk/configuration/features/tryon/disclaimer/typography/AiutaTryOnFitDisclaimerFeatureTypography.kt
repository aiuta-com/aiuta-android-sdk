package com.aiuta.fashionsdk.configuration.features.tryon.disclaimer.typography

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp

/**
 * Interface defining typography styles for the fit disclaimer.
 */
public interface AiutaTryOnFitDisclaimerFeatureTypography {
    /**
     * Text style for the fit disclaimer bar.
     */
    public val disclaimer: TextStyle

    /**
     * Default implementation of [AiutaTryOnFitDisclaimerFeatureTypography].
     *
     * Provides standard typography settings.
     */
    public class Default(
        override val disclaimer: TextStyle,
    ) : AiutaTryOnFitDisclaimerFeatureTypography {
        public constructor() : this(
            disclaimer = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                letterSpacing = TextUnit(-0.01f, TextUnitType.Sp),
            ),
        )
    }
}
