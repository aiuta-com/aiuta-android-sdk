package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.appbar.AiutaAppBar
import com.aiuta.fashionsdk.compose.uikit.appbar.AiutaAppBarIcon
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.QuestionaryStep

@Composable
internal fun QuestionaryAppBar(
    stepState: State<QuestionaryStep>,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navigationController = LocalAiutaNavigationController.current
    val theme = LocalTheme.current

    val isNavigationIconVisible = remember {
        derivedStateOf {
            stepState.value != QuestionaryStep.FindSizeStep
        }
    }
    val navigationIconRotateAngle = if (theme.pageBar.toggles.preferCloseButtonOnTheRight) {
        0f
    } else {
        180f
    }

    AiutaAppBar(
        modifier = modifier,
        navigationIcon = { navigationIconModifier ->
            AnimatedVisibility(
                modifier = navigationIconModifier,
                visible = isNavigationIconVisible.value,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                AiutaAppBarIcon(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .graphicsLayer { rotationZ = navigationIconRotateAngle },
                    icon = theme.pageBar.icons.back24,
                    color = theme.color.primary,
                    onClick = navigateBack,
                )
            }
        },
        title = { titleModifier ->
            AppBarProgress(
                stepState = stepState,
                modifier = titleModifier
                    .height(10.dp)
                    .width(120.dp),
            )
        },
        closeButton = { actionModifier ->
            AiutaAppBarIcon(
                modifier = actionModifier,
                icon = theme.pageBar.icons.close24,
                color = theme.color.primary,
                onClick = navigationController::clickClose,
            )
        },
    )
}

@Composable
private fun AppBarProgress(
    stepState: State<QuestionaryStep>,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current
    val sharedShape = RoundedCornerShape(8.dp)

    val fraction = animateFloatAsState(
        targetValue = when (stepState.value) {
            QuestionaryStep.FindSizeStep -> 0.1f
            QuestionaryStep.BellyShapeStep -> 0.5f
            QuestionaryStep.BraStep -> 1f
        },
    )

    Box(
        modifier = modifier.background(
            color = theme.color.neutral,
            shape = sharedShape,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = fraction.value)
                .background(
                    color = theme.color.brand,
                    shape = sharedShape,
                ),
        )
    }
}
