package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.tokens.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.screen.model.ModelSelectorScreenState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaTryOnDataController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaTryOnStringResources
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.appbar.ModelSelectorAppBar
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.content.ModelSelectorShowContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.error.ModelSelectorEmptyModelsErrorContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.error.ModelSelectorGeneralErrorContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.loading.ModelSelectorLoadingContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.controller.initModelSelectorScreen

@Composable
internal fun ModelSelectorScreen(modifier: Modifier = Modifier) {
    val theme = LocalTheme.current
    val dataController = LocalAiutaTryOnDataController.current
    val stringResources = LocalAiutaTryOnStringResources.current

    val screenState =
        remember { mutableStateOf<ModelSelectorScreenState>(ModelSelectorScreenState.Loading) }
    val screenStateTransition = updateTransition(screenState.value)

    val sharedModifier = Modifier.fillMaxSize()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        initModelSelectorScreen(
            dataController = dataController,
            screenState = screenState,
            stringResources = stringResources,
        )
    }

    Column(
        modifier =
        modifier
            .background(theme.colors.background)
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ModelSelectorAppBar(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Spacer(Modifier.height(16.dp))

        screenStateTransition.AnimatedContent(
            modifier = Modifier.fillMaxSize(),
        ) { state ->
            when (state) {
                is ModelSelectorScreenState.Content -> {
                    ModelSelectorShowContent(
                        modifier = sharedModifier,
                        state = state,
                    )
                }

                is ModelSelectorScreenState.EmptyModelsListError -> {
                    ModelSelectorEmptyModelsErrorContent(modifier = sharedModifier)
                }

                is ModelSelectorScreenState.GeneralError -> {
                    ModelSelectorGeneralErrorContent(
                        modifier = sharedModifier,
                        onRetry = {
                            scope.initModelSelectorScreen(
                                dataController = dataController,
                                screenState = screenState,
                                stringResources = stringResources,
                                forceUpdate = true,
                            )
                        },
                    )
                }

                is ModelSelectorScreenState.Loading -> {
                    ModelSelectorLoadingContent(modifier = sharedModifier)
                }
            }
        }
    }
}
