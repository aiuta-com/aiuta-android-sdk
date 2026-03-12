package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.GeneratedImageUIModel

@Composable
internal fun HistoryScreenListeners(generatedImages: LazyPagingItems<GeneratedImageUIModel>) {
    val navigationController = LocalAiutaNavigationController.current

    // If we have empty history, let's navigate back
    LaunchedEffect(
        generatedImages.itemCount,
        generatedImages.loadState.refresh,
    ) {
        if (generatedImages.itemCount == 0 && generatedImages.loadState.refresh is LoadState.NotLoading) {
            navigationController.navigateBack()
        }
    }
}
