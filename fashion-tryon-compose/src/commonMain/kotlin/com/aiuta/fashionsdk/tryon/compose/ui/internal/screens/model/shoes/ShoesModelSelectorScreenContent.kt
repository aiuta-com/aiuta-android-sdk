package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.shoes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.error.ModelSelectorEmptyModelsErrorContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.error.ModelSelectorGeneralErrorContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.shoes.blocks.shoesModelsBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.shoes.components.appbar.ShoesModelSelectorAppBar
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.shoes.components.loading.ShoesModelSelectorLoadingContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.shoes.models.ShoesModelSelectorScreenEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.shoes.models.ShoesModelSelectorScreenViewState

@Composable
internal fun ShoesModelSelectorScreenContent(
    viewState: State<ShoesModelSelectorScreenViewState>,
    eventHandler: (ShoesModelSelectorScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current

    val sharedModifier = Modifier.fillMaxSize()
    val statusTransition = updateTransition(viewState.value.status)

    Column(
        modifier = modifier
            .background(theme.color.background)
            .windowInsetsPadding(WindowInsets.navigationBars),
    ) {
        ShoesModelSelectorAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            genders = viewState.value.genders,
            activeGenderId = viewState.value.activeGender?.id,
            onGenderClick = { genderId ->
                eventHandler(ShoesModelSelectorScreenEvent.GenderSelected(genderId))
            },
            onBack = { eventHandler(ShoesModelSelectorScreenEvent.BackClicked) },
        )

        Spacer(Modifier.height(16.dp))

        statusTransition.AnimatedContent(
            modifier = Modifier.fillMaxSize(),
        ) { status ->
            when (status) {
                ShoesModelSelectorScreenViewState.Status.CONTENT -> {
                    LazyColumn(
                        modifier = sharedModifier,
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        viewState.value.activeGender?.let { activeGender ->
                            shoesModelsBlock(
                                activeGender = activeGender,
                                theme = theme,
                                eventHandler = eventHandler,
                            )
                        }
                    }
                }

                ShoesModelSelectorScreenViewState.Status.EMPTY_MODELS_ERROR -> {
                    ModelSelectorEmptyModelsErrorContent(modifier = sharedModifier)
                }

                ShoesModelSelectorScreenViewState.Status.GENERAL_ERROR -> {
                    ModelSelectorGeneralErrorContent(
                        modifier = sharedModifier,
                        onRetry = { eventHandler(ShoesModelSelectorScreenEvent.RetryClicked) },
                    )
                }

                ShoesModelSelectorScreenViewState.Status.LOADING -> {
                    ShoesModelSelectorLoadingContent(modifier = sharedModifier)
                }
            }
        }
    }
}
