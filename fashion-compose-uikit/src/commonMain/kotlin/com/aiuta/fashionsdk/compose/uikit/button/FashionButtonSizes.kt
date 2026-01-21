package com.aiuta.fashionsdk.compose.uikit.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.button.internal.DefaultFashionButtonSize
import com.aiuta.fashionsdk.compose.uikit.button.internal.FashionButtonSize
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme

/**
 * A collection of predefined button sizes for the Fashion SDK.
 * These sizes provide consistent dimensions across the application.
 */
@Immutable
public object FashionButtonSizes {
    private val IconSize = 20.dp
    private val IconSpacing = 4.dp

    /**
     * Creates a large button size with customizable dimensions and styling.
     *
     * @param verticalPadding The vertical padding of the button
     * @param horizontalPadding The horizontal padding of the button
     * @param iconSize The size of the icon within the button
     * @param iconSpacing The spacing between the icon and text
     * @param shape The shape of the button
     * @param textStyle The text style for the button's text
     * @return A large button size configuration
     */
    @Composable
    public fun lSize(
        verticalPadding: Dp = 16.dp,
        horizontalPadding: Dp = 16.dp,
        iconSize: Dp = IconSize,
        iconSpacing: Dp = IconSpacing,
        shape: Shape = LocalTheme.current.button.shapes.buttonMShape,
        textStyle: TextStyle = LocalTheme.current.button.typography.buttonM,
    ): FashionButtonSize = DefaultFashionButtonSize(
        paddingValues =
        PaddingValues(
            vertical = verticalPadding,
            horizontal = horizontalPadding,
        ),
        iconSize = iconSize,
        iconSpacing = iconSpacing,
        shape = shape,
        textStyle = textStyle,
        minHeight = 50.dp,
    )

    /**
     * Creates a medium button size with customizable dimensions and styling.
     *
     * @param verticalPadding The vertical padding of the button
     * @param horizontalPadding The horizontal padding of the button
     * @param iconSize The size of the icon within the button
     * @param iconSpacing The spacing between the icon and text
     * @param shape The shape of the button
     * @param textStyle The text style for the button's text
     * @return A medium button size configuration
     */
    @Composable
    public fun mSize(
        verticalPadding: Dp = 12.dp,
        horizontalPadding: Dp = 24.dp,
        iconSize: Dp = IconSize,
        iconSpacing: Dp = IconSpacing,
        shape: Shape = LocalTheme.current.button.shapes.buttonSShape,
        textStyle: TextStyle = LocalTheme.current.button.typography.buttonS,
    ): FashionButtonSize = DefaultFashionButtonSize(
        paddingValues =
        PaddingValues(
            vertical = verticalPadding,
            horizontal = horizontalPadding,
        ),
        iconSize = iconSize,
        iconSpacing = iconSpacing,
        shape = shape,
        textStyle = textStyle,
        minHeight = 42.dp,
    )
}
