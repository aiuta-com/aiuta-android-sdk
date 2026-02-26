package com.aiuta.fashionsdk.tryon.compose.ui.internal.components.icons

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import kotlinx.coroutines.delay

@Composable
internal fun AiutaLoadingComponent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    circleSize: Dp = 14.dp,
    circleColor: Color = LocalTheme.current.color.brand,
    component: @Composable () -> Unit,
) {
    val theme = LocalTheme.current
    val isProgressVisible = remember { mutableStateOf(false) }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(theme.activityIndicator.settings.indicationDelay.toLong())
            isProgressVisible.value = true
        } else {
            isProgressVisible.value = false
        }
    }

    AnimatedContent(
        modifier = modifier,
        targetState = isProgressVisible.value,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        contentAlignment = Alignment.Center,
    ) { isOperationLoading ->
        if (isOperationLoading) {
            AiutaLoadingIcon(
                modifier = Modifier.size(circleSize),
                circleColor = circleColor,
            )
        } else {
            component()
        }
    }
}
