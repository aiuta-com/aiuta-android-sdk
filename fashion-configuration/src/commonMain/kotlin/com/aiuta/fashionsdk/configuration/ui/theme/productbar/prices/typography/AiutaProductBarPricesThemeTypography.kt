package com.aiuta.fashionsdk.configuration.ui.theme.productbar.prices.typography

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp

/**
 * Typography configuration for price displays in product bars.
 *
 * This interface defines text styles used for displaying prices throughout
 * the SDK. It provides consistent price text styling that can be customized
 * to match your app's design system.
 *
 *
 * @property price Text style for displaying prices
 */
public interface AiutaProductBarPricesThemeTypography {
    public val price: TextStyle

    /**
     * Default implementation of [AiutaProductBarPricesThemeTypography].
     *
     * This class provides standard text styles for price displays:
     * - Price: Bold weight, 16sp font size, 18sp line height
     *
     * ```kotlin
     * theme {
     *     productBar {
     *         prices {
     *             typography = AiutaProductBarPricesThemeTypography.Default()
     *         }
     *     }
     * }
     * ```
     */
    public class Default : AiutaProductBarPricesThemeTypography {
        override val price: TextStyle = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            letterSpacing = TextUnit(-0.01f, TextUnitType.Sp),
        )
    }
}
