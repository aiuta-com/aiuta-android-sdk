package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.content

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.config.features.TryOnModelsCategoryUiModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.screen.model.ModelSelectorScreenState

@Composable
internal fun ModelsCategoriesBlock(
    modifier: Modifier = Modifier,
    state: ModelSelectorScreenState.Content,
    activeCategory: MutableState<TryOnModelsCategoryUiModel?>,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        state.categories.forEach { category ->
            key(category.category) {
                ModelsCategoryBlock(
                    category = category.category,
                    isActive = category == activeCategory.value,
                    onClick = {
                        activeCategory.value = category
                    },
                )
            }
        }
    }
}

@Composable
internal fun ModelsCategoryBlock(
    modifier: Modifier = Modifier,
    category: String,
    isActive: Boolean,
    onClick: () -> Unit,
) {
    val theme = LocalTheme.current

    val sharedModifier = modifier.clickableUnindicated { onClick() }

    val textColor =
        animateColorAsState(
            targetValue =
            if (isActive) {
                theme.color.primary
            } else {
                theme.color.secondary
            },
        )

    Text(
        modifier =
        if (isActive) {
            sharedModifier.drawBehind {
                val strokeWidthPx = 4.dp.toPx()
                val verticalOffset = size.height + 6.sp.toPx()

                val horizontalPadding = 5.dp.toPx()

                drawLine(
                    color = theme.color.brand,
                    strokeWidth = strokeWidthPx,
                    start = Offset(-horizontalPadding, verticalOffset),
                    end = Offset(size.width + horizontalPadding, verticalOffset),
                    cap = StrokeCap.Round,
                )
            }
        } else {
            sharedModifier
        },
        text = category,
        style = theme.button.typography.buttonS,
        color = textColor.value,
        textAlign = TextAlign.Center,
    )
}
