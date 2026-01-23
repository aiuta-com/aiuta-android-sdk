package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated

@Composable
internal fun VariantBox(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: AiutaIcon? = null,
) {
    val theme = LocalTheme.current

    val sharedShape = RoundedCornerShape(16.dp)
    val borderColor = animateColorAsState(
        targetValue = if (isSelected) {
            theme.color.brand
        } else {
            theme.color.neutral
        },
    )

    Box(
        modifier = modifier
            .clip(sharedShape)
            .background(theme.color.background)
            .border(
                width = 1.dp,
                color = borderColor.value,
                shape = sharedShape,
            )
            .clickableUnindicated(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            icon?.let {
                AiutaIcon(
                    icon = icon,
                    contentDescription = null,
                    tint = theme.color.brand,
                )

                Spacer(Modifier.width(8.dp))
            }

            Text(
                text = text,
                style = theme.label.typography.regular,
                color = theme.color.primary,
                textAlign = TextAlign.Start,
            )
        }
    }
}
