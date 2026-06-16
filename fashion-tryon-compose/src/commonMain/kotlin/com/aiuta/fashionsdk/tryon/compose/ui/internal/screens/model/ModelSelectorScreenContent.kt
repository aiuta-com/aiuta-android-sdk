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
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.appbar.ModelSelectorAppBar
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.content.ModelSelectorShowContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.error.ModelSelectorEmptyModelsErrorContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.error.ModelSelectorGeneralErrorContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.loading.ModelSelectorLoadingContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.models.ModelSelectorScreenEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.models.ModelSelectorScreenViewState

@Composable
internal fun ModelSelectorScreenContent(
    viewState: State<ModelSelectorScreenViewState>,
    eventHandler: (ModelSelectorScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current

    val sharedModifier = Modifier.fillMaxSize()
    val statusTransition = updateTransition(viewState.value.status)

    Column(
        modifier = modifier
            .background(theme.color.background)
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ModelSelectorAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onBack = { eventHandler(ModelSelectorScreenEvent.BackClicked) },
        )

        Spacer(Modifier.height(16.dp))

        statusTransition.AnimatedContent(
            modifier = Modifier.fillMaxSize(),
        ) { status ->
            when (status) {
                ModelSelectorScreenViewState.Status.CONTENT -> {
                    ModelSelectorShowContent(
                        modifier = sharedModifier,
                        viewState = viewState,
                        eventHandler = eventHandler,
                    )
                }

                ModelSelectorScreenViewState.Status.EMPTY_MODELS_ERROR -> {
                    ModelSelectorEmptyModelsErrorContent(modifier = sharedModifier)
                }

                ModelSelectorScreenViewState.Status.GENERAL_ERROR -> {
                    ModelSelectorGeneralErrorContent(
                        modifier = sharedModifier,
                        onRetry = { eventHandler(ModelSelectorScreenEvent.RetryClicked) },
                    )
                }

                ModelSelectorScreenViewState.Status.LOADING -> {
                    ModelSelectorLoadingContent(modifier = sharedModifier)
                }
            }
        }
    }
}
