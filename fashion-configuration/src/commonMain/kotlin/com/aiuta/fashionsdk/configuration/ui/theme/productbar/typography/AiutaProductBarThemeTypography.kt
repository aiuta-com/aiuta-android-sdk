package com.aiuta.fashionsdk.configuration.ui.theme.productbar.typography

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp

/**
 * Typography configuration for product bar text elements.
 *
 * This interface defines text styles used for product information in the product
 * bar throughout the SDK. It provides consistent text styling that can be
 * customized to match your app's design system.
 *
 * ```kotlin
 * theme {
 *     productBar {
 *         typography = object : AiutaProductBarThemeTypography {
 *             override val product = TextStyle(
 *                 fontFamily = FontFamily.Default,
 *                 fontWeight = FontWeight.Medium,
 *                 fontSize = 12.sp,
 *                 lineHeight = 18.sp
 *             )
 *             override val brand = TextStyle(
 *                 fontFamily = FontFamily.Default,
 *                 fontWeight = FontWeight.Medium,
 *                 fontSize = 10.sp,
 *                 lineHeight = 16.sp,
 *                 letterSpacing = 0.04.sp
 *             )
 *         }
 *     }
 * }
 * ```
 *
 * @property product Text style for product names
 * @property brand Text style for brand names
 */
public interface AiutaProductBarThemeTypography {
    public val product: TextStyle
    public val brand: TextStyle

    /**
     * Default implementation of [AiutaProductBarThemeTypography].
     *
     * This class provides standard text styles for product bar elements:
     * - Product name: Medium weight, 12sp font size, 18sp line height
     * - Brand name: Medium weight, 10sp font size, 16sp line height, 0.04sp letter spacing
     *
     * ```kotlin
     * theme {
     *     productBar {
     *         typography = AiutaProductBarThemeTypography.Default()
     *     }
     * }
     * ```
     */
    public class Default : AiutaProductBarThemeTypography {
        override val product: TextStyle = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
        )
        override val brand: TextStyle = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            letterSpacing = TextUnit(-0.01f, TextUnitType.Sp),
        )
    }
}
