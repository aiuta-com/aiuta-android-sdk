package com.aiuta.fashionsdk.compose.uikit.button.internal

import androidx.compose.material.ButtonColors
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
public interface FashionButtonColor {
    public val buttonColors: ButtonColors
    public val rippleColor: Color
}

internal data class DefaultFashionButtonColor(
    override val buttonColors: ButtonColors,
    override val rippleColor: Color,
) : FashionButtonColor

internal data class OutlineFashionButtonColor(
    override val buttonColors: ButtonColors,
    override val rippleColor: Color,
    val borderColor: Color,
) : FashionButtonColor
