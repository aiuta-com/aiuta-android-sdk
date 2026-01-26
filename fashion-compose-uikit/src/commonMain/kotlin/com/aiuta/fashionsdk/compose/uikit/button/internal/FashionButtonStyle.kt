package com.aiuta.fashionsdk.compose.uikit.button.internal

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush

@Immutable
public interface FashionButtonStyle {
    public val colors: FashionButtonColor
    public val border: BorderStroke?
}

internal data class DefaultFashionButtonStyle(
    override val colors: DefaultFashionButtonColor,
    override val border: BorderStroke? = null,
) : FashionButtonStyle

internal data class SecondaryFashionButtonStyle(
    override val colors: OutlineFashionButtonColor,
    override val border: BorderStroke? =
        BorderStroke(
            ButtonDefaults.OutlinedBorderSize,
            colors.borderColor,
        ),
) : FashionButtonStyle

internal data class GradientButtonStyle(
    override val colors: DefaultFashionButtonColor,
    override val border: BorderStroke? = null,
    val gradientBackground: Brush? = null,
) : FashionButtonStyle
