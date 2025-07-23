package com.aiuta.fashionsdk.configuration.ui.theme.label.typography

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp

/**
 * Typography configuration for label text elements.
 *
 * This interface defines text styles used for different types of labels in the SDK,
 * including titles and regular text. It provides consistent text styling that can
 * be customized to match your app's design system.
 *
 *
 * @property titleL Text style for large titles
 * @property titleM Text style for medium titles
 * @property regular Text style for regular label text
 * @property subtle Text style for subtle or secondary label text
 */
public interface AiutaLabelThemeTypography {
    public val titleL: TextStyle
    public val titleM: TextStyle
    public val regular: TextStyle
    public val subtle: TextStyle

    /**
     * Default implementation of [AiutaLabelThemeTypography].
     *
     * This class provides standard text styles for labels.
     *
     * ```kotlin
     * theme {
     *     label {
     *         typography = AiutaLabelThemeTypography.Default()
     *     }
     * }
     * ```
     */
    public class Default : AiutaLabelThemeTypography {
        override val titleL: TextStyle = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
        )
        override val titleM: TextStyle = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            letterSpacing = TextUnit(-0.02f, TextUnitType.Sp),
        )
        override val regular: TextStyle = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 17.sp,
            lineHeight = 22.sp,
            letterSpacing = TextUnit(-0.03f, TextUnitType.Sp),
        )
        override val subtle: TextStyle = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            lineHeight = 18.sp,
            letterSpacing = TextUnit(-0.01f, TextUnitType.Sp),
        )
    }
}
