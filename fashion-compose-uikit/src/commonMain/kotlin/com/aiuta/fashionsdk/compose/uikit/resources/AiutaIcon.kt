package com.aiuta.fashionsdk.compose.uikit.resources

import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.resources.painter.painterResource

/**
 * A composable that displays an icon from the Fashion SDK's icon resources.
 * The icon can be tinted according to the current content color and alpha,
 * unless the icon is configured to be drawn as-is.
 *
 * @param icon The icon resource to display
 * @param contentDescription The content description for accessibility
 * @param modifier The modifier to be applied to the icon
 * @param tint The color to tint the icon with. Defaults to the current content color with alpha
 */
@Composable
public fun AiutaIcon(
    icon: AiutaIcon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
) {
    Icon(
        painter = painterResource(drawableResource = icon.iconResource),
        contentDescription = contentDescription,
        modifier = modifier,
        tint = if (icon.shouldDrawAsIs) Color.Unspecified else tint,
    )
}
