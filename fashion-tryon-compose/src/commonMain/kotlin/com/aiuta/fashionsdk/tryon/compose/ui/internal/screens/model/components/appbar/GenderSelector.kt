package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.appbar

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.core.size.rememberScreenSize
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.models.GenderTabUiModel

@Composable
internal fun GenderSelector(
    genders: List<GenderTabUiModel>,
    activeGenderId: String?,
    onGenderClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current

    val screenSize = rememberScreenSize()
    val selectorWidth = screenSize.widthDp * 0.6f

    Row(
        modifier = modifier
            .width(selectorWidth)
            .height(40.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(theme.color.neutral)
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        genders.forEach { gender ->
            key(gender.id) {
                GenderSelectorItem(
                    modifier = Modifier.fillMaxHeight().weight(1f),
                    title = gender.title,
                    isActive = gender.id == activeGenderId,
                    onClick = { onGenderClick(gender.id) },
                )
            }
        }
    }
}

@Composable
private fun GenderSelectorItem(
    title: String,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current

    val backgroundColor = animateColorAsState(
        targetValue = if (isActive) theme.color.background else theme.color.background.copy(alpha = 0f),
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .background(backgroundColor.value)
            .clickableUnindicated { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title,
            style = theme.button.typography.buttonS,
            color = theme.color.primary,
        )
    }
}
