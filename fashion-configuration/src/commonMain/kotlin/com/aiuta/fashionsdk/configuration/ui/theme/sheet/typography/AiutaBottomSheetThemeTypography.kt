package com.aiuta.fashionsdk.configuration.ui.theme.sheet.typography

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp

/**
 * Typography configuration for bottom sheet text elements.
 *
 * This interface defines text styles used for various text elements within
 * bottom sheets, including icon buttons and chip buttons. It provides
 * consistent text styling that can be customized to match your app's design.
 *
 *
 * @property iconButton Text style for icon buttons in the bottom sheet
 * @property chipsButton Text style for chip-style buttons in the bottom sheet
 */
public interface AiutaBottomSheetThemeTypography {
    public val iconButton: TextStyle
    public val chipsButton: TextStyle

    /**
     * Default implementation of [AiutaBottomSheetThemeTypography].
     *
     * This class provides standard text styles for bottom sheet elements.
     *
     * ```kotlin
     * theme {
     *     bottomSheet {
     *         typography = AiutaBottomSheetThemeTypography.Default()
     *     }
     * }
     * ```
     */
    public class Default : AiutaBottomSheetThemeTypography {
        override val iconButton: TextStyle = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 17.sp,
            letterSpacing = TextUnit(-0.01f, TextUnitType.Sp),
        )
        override val chipsButton: TextStyle = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 18.sp,
        )
    }
}
