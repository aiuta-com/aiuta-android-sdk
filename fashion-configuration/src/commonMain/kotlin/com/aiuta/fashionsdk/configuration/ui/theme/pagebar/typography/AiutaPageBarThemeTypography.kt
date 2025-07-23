package com.aiuta.fashionsdk.configuration.ui.theme.pagebar.typography

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp

/**
 * Typography configuration for page bar text elements.
 *
 * This interface defines text styles used for page titles and other text elements
 * in the page navigation bar. It provides consistent text styling that can be
 * customized to match your app's design system.
 *
 *
 * @property pageTitle Text style for the page title in the navigation bar
 */
public interface AiutaPageBarThemeTypography {
    public val pageTitle: TextStyle

    /**
     * Default implementation of [AiutaPageBarThemeTypography].
     *
     * This class provides standard text styles for page bar elements.
     *
     * ```kotlin
     * theme {
     *     pageBar {
     *         typography = AiutaPageBarThemeTypography.Default()
     *     }
     * }
     * ```
     */
    public class Default : AiutaPageBarThemeTypography {
        override val pageTitle: TextStyle = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 17.sp,
            lineHeight = 22.sp,
            letterSpacing = TextUnit(-0.03f, TextUnitType.Sp),
        )
    }
}
