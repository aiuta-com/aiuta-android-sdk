package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.configuration.features.share.AiutaShareFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.models.SelectorMode
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.provideFeature

@Composable
internal fun SelectorCard(
    modifier: Modifier = Modifier,
    selectionMode: MutableState<SelectorMode>,
    isActionActive: Boolean,
    onSelectAll: () -> Unit,
    onDeselectAll: () -> Unit,
    onCancel: () -> Unit,
    isShareLoading: Boolean = false,
    onShare: () -> Unit,
    onDelete: () -> Unit,
) {
    val theme = LocalTheme.current

    val shareFeature = provideFeature<AiutaShareFeature>()

    Row(
        modifier =
        modifier
            .background(
                color = theme.color.primary,
                shape = theme.button.shapes.buttonMShape,
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(
                text = theme.selectionSnackbar.strings.cancel,
                backgroundColor = theme.color.onDark,
                textColor = theme.color.primary,
            ) {
                onCancel()
            }

            AnimatedContent(
                targetState = selectionMode.value,
                transitionSpec = {
                    slideInVertically(
                        animationSpec = tween(durationMillis = 400),
                        initialOffsetY = { -it },
                    ) + fadeIn() togetherWith slideOutVertically(
                        animationSpec = tween(durationMillis = 400),
                        targetOffsetY = { it },
                    ) + fadeOut()
                },
                label = "selectable button",
            ) { state ->
                TextButton(
                    text =
                    if (state == SelectorMode.ALL_IS_SELECTED) {
                        theme.selectionSnackbar.strings.unselectAll
                    } else {
                        theme.selectionSnackbar.strings.selectAll
                    },
                    backgroundColor = Color.Transparent,
                    textColor = theme.color.onDark,
                ) {
                    if (state == SelectorMode.ALL_IS_SELECTED) {
                        onDeselectAll()
                    } else {
                        onSelectAll()
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            SmallIconButton(
                icon = theme.selectionSnackbar.icons.trash24,
                isActive = isActionActive,
            ) {
                onDelete()
            }

            shareFeature?.let {
                Spacer(Modifier.width(16.dp))

                SmallIconButton(
                    icon = shareFeature.icons.share24,
                    isLoading = isShareLoading,
                    isActive = isActionActive,
                ) {
                    onShare()
                }
            }
        }
    }
}
