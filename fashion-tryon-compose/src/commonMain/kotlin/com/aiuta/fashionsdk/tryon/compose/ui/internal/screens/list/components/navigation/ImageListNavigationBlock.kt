package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.list.components.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.GeneratedImageUIModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.navigateBack
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.paging.LazyPagingItems
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.tryon.compose.uikit.utils.clickableUnindicated

@Composable
internal fun ImageListNavigationBlock(
    modifier: Modifier,
    pagerState: PagerState,
    generatedImages: LazyPagingItems<GeneratedImageUIModel>,
) {
    val controller = LocalController.current
    val theme = LocalTheme.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AiutaIcon(
            modifier = Modifier
                .size(24.dp)
                .clickableUnindicated(onClick = controller::navigateBack),
            icon = theme.pageBar.icons.close24,
            contentDescription = null,
            tint = theme.color.onDark,
        )

        Spacer(Modifier.height(40.dp))

        GenerationIndicator(
            modifier = Modifier.fillMaxHeight(),
            pagerState = pagerState,
            generatedImages = generatedImages,
        )
    }
}
