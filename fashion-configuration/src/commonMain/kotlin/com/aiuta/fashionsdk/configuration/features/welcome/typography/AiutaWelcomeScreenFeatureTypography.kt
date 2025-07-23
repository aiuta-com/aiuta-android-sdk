package com.aiuta.fashionsdk.configuration.features.welcome.typography

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Interface defining typography styles for the welcome screen.
 *
 * This interface provides text styles for different text elements
 * displayed on the welcome screen.
 */
public interface AiutaWelcomeScreenFeatureTypography {
    /**
     * Text style for the welcome screen title.
     * Uses a large, bold font to create visual hierarchy.
     */
    public val welcomeTitle: TextStyle

    /**
     * Text style for the welcome screen description.
     * Uses a medium weight, readable font for body text.
     */
    public val welcomeDescription: TextStyle

    /**
     * Default implementation of [AiutaWelcomeScreenFeatureTypography].
     *
     * Provides standard typography settings.
     */
    public class Default(
        override val welcomeTitle: TextStyle,
        override val welcomeDescription: TextStyle,
    ) : AiutaWelcomeScreenFeatureTypography {
        public constructor() : this(
            welcomeTitle =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Black,
                fontSize = 40.sp,
                lineHeight = 44.sp,
            ),
            welcomeDescription =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 18.sp,
            ),
        )
    }
}
