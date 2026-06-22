package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.blocks.addtocart.addToCartBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.blocks.appbar.appBarBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.blocks.photo.photoBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.blocks.products.productsBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.ThanksFeedbackBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models.GenerationResultScreenEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models.GenerationResultScreenViewState

@Composable
internal fun GenerationResultScreenContent(
    viewState: State<GenerationResultScreenViewState>,
    eventHandler: (GenerationResultScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current

    // Genuine scaffold state stays in the composable, not in the ViewState.
    val pagerState = rememberPagerState(pageCount = { viewState.value.generations.size })
    val lazyListState = rememberLazyListState()

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(theme.color.background)
            .windowInsetsPadding(WindowInsets.navigationBars),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
        ) {
            appBarBlock(viewState = viewState)

            photoBlock(
                viewState = viewState,
                pagerState = pagerState,
                availableHeight = maxHeight,
                eventHandler = eventHandler,
            )

            addToCartBlock(
                viewState = viewState,
                eventHandler = eventHandler,
            )

            productsBlock(
                viewState = viewState,
                eventHandler = eventHandler,
            )
        }

        ThanksFeedbackBlock(
            modifier = Modifier.align(Alignment.Center),
            isVisible = viewState.value.isThanksFeedbackVisible,
        )
    }
}
