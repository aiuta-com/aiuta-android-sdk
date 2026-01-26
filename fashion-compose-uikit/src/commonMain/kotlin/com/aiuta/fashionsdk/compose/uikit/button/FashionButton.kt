@file:Suppress("LongParameterList")

package com.aiuta.fashionsdk.compose.uikit.button

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.elevation
import androidx.compose.material.ButtonElevation
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.button.internal.FashionButtonLoadingAnimation
import com.aiuta.fashionsdk.compose.uikit.button.internal.FashionButtonSize
import com.aiuta.fashionsdk.compose.uikit.button.internal.FashionButtonStyle
import com.aiuta.fashionsdk.compose.uikit.button.internal.GradientButtonStyle
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.utils.conditional

/**
 * A customizable button component that supports various styles, sizes, and states.
 * The button can display text, an icon, and a loading animation.
 *
 * @param modifier The modifier to be applied to the button
 * @param contentDescription The content description for accessibility
 * @param elevation The elevation of the button
 * @param icon Optional icon to be displayed in the button
 * @param isEnable Whether the button is enabled
 * @param isLoading Whether the button is in a loading state
 * @param text Optional text to be displayed in the button
 * @param style The style of the button (e.g., gradient, solid)
 * @param size The size of the button
 * @param onClick The callback to be invoked when the button is clicked
 */
@Composable
public fun FashionButton(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    elevation: ButtonElevation = elevation(
        defaultElevation = 0.dp,
        pressedElevation = 0.dp,
    ),
    icon: AiutaIcon? = null,
    isEnable: Boolean = true,
    isLoading: Boolean = false,
    text: String? = null,
    style: FashionButtonStyle,
    size: FashionButtonSize,
    onClick: () -> Unit,
) {
    val transition = updateTransition(isLoading, label = "loading button state")

    Button(
        modifier = modifier
            .heightIn(min = size.minHeight)
            .conditional(style is GradientButtonStyle) {
                (style as? GradientButtonStyle)?.gradientBackground?.let {
                    background(
                        brush = it,
                        shape = size.shape,
                    )
                } ?: this
            },
        shape = size.shape,
        border = style.border,
        colors = style.colors.buttonColors,
        contentPadding = size.paddingValues,
        elevation = elevation,
        enabled = isEnable,
        onClick = {
            if (!isLoading) {
                onClick()
            }
        },
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            transition.AnimatedContent { loading ->
                if (loading) {
                    FashionButtonLoadingAnimation()
                } else {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        icon?.let {
                            AiutaIcon(
                                modifier = Modifier.size(size.iconSize),
                                tint =
                                style.colors.buttonColors.contentColor(
                                    enabled = isEnable,
                                ).value,
                                icon = icon,
                                contentDescription = contentDescription,
                            )
                        }
                        text?.let {
                            Text(
                                style = size.textStyle,
                                textAlign = TextAlign.Center,
                                color =
                                style.colors.buttonColors.contentColor(
                                    enabled = isEnable,
                                ).value,
                                overflow = TextOverflow.Ellipsis,
                                modifier =
                                Modifier.padding(
                                    start =
                                    if (icon != null) {
                                        size.iconSpacing
                                    } else {
                                        0.dp
                                    },
                                ),
                                text = text,
                            )
                        }
                    }
                }
            }
        }
    }
}
