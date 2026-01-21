package com.aiuta.fashionsdk.compose.uikit.button.internal

import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.aiuta.fashionsdk.configuration.ui.theme.AiutaTheme

internal object FashionButtonColors {
    @Composable
    fun primaryColors(theme: AiutaTheme) = DefaultFashionButtonColor(
        buttonColors = ButtonDefaults.buttonColors(
            backgroundColor = theme.color.brand,
            contentColor = theme.color.onDark,
        ),
        rippleColor = theme.color.brand.copy(alpha = 0.1f),
    )

    @Composable
    fun primaryColors(
        backgroundColor: Color,
        contentColor: Color,
    ) = DefaultFashionButtonColor(
        buttonColors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor,
        ),
        rippleColor = backgroundColor.copy(alpha = 0.1f),
    )

    @Composable
    fun secondaryColors(theme: AiutaTheme) = OutlineFashionButtonColor(
        buttonColors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = theme.color.background,
            contentColor = theme.color.primary,
        ),
        borderColor = theme.color.border,
        rippleColor = theme.color.primary.copy(alpha = 0.05f),
    )

    @Composable
    fun transparentColors(contentColor: Color) = DefaultFashionButtonColor(
        buttonColors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = contentColor,
        ),
        rippleColor = Color.Black.copy(alpha = 0.05f),
    )

    @Composable
    fun secondaryColors(
        backgroundColor: Color,
        contentColor: Color,
        borderColor: Color,
    ) = OutlineFashionButtonColor(
        buttonColors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor,
        ),
        borderColor = borderColor,
        rippleColor = contentColor.copy(alpha = 0.05f),
    )
}
