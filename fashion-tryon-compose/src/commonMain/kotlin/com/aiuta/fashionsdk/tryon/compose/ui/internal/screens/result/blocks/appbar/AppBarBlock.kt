package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.blocks.appbar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.appbar.MainAppBar
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models.GenerationResultScreenViewState

@OptIn(ExperimentalFoundationApi::class)
internal fun LazyListScope.appBarBlock(
    viewState: State<GenerationResultScreenViewState>,
) {
    stickyHeader(
        key = "app_bar_block",
        contentType = "app_bar_block",
    ) {
        val theme = LocalTheme.current

        MainAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(theme.color.background)
                .padding(start = 16.dp, end = 16.dp, bottom = 6.dp),
            title = viewState.value.title,
        )
    }
}
