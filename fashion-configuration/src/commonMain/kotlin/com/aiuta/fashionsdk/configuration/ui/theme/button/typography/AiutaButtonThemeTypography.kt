package com.aiuta.fashionsdk.configuration.ui.theme.button.typography

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp

/**
 * Typography configuration for button text elements.
 *
 * This interface defines text styles used for different sizes of buttons in the SDK.
 * It provides consistent text styling that can be customized to match your app's
 * design system.
 *
 *
 * @property buttonM Text style for medium-sized buttons
 * @property buttonS Text style for small-sized buttons
 */
public interface AiutaButtonThemeTypography {
    public val buttonM: TextStyle
    public val buttonS: TextStyle

    /**
     * Default implementation of [AiutaButtonThemeTypography].
     *
     * This class provides standard text styles for buttons:
     * - Medium buttons: Bold weight, 14sp font size, 20sp line height
     * - Small buttons: Medium weight, 13sp font size, 18sp line height
     *
     * ```kotlin
     * theme {
     *     button {
     *         typography = AiutaButtonThemeTypography.Default()
     *     }
     * }
     * ```
     */
    public class Default : AiutaButtonThemeTypography {
        override val buttonM: TextStyle = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.SemiBold,
            fontSize = 17.sp,
            lineHeight = 18.sp,
            letterSpacing = TextUnit(-0.01f, TextUnitType.Sp),
        )
        override val buttonS: TextStyle = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            letterSpacing = TextUnit(-0.01f, TextUnitType.Sp),
        )
    }
}
