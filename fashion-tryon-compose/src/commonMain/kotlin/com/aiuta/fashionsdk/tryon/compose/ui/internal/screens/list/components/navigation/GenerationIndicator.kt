package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.list.components.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.GeneratedImageUIModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.list.utils.fadingEdge
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.paging.LazyPagingItems
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.paging.itemContentType
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.paging.itemKey
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.tryon.compose.uikit.utils.clickableUnindicated
import kotlinx.coroutines.launch

@Composable
internal fun GenerationIndicator(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    generatedImages: LazyPagingItems<GeneratedImageUIModel>,
) {
    val theme = LocalTheme.current

    val scope = rememberCoroutineScope()
    val indicatorState = rememberLazyListState()
    val fadeBrush = Brush.verticalGradient(
        0.0f to Color.Transparent,
        0.04f to theme.color.onDark,
        0.96f to theme.color.onDark,
        1.0f to Color.Transparent,
    )

    LaunchedEffect(pagerState.settledPage) {
        indicatorState.animateScrollToItem(pagerState.settledPage)
    }

    LazyColumn(
        modifier = modifier.fadingEdge(brush = fadeBrush),
        state = indicatorState,
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Bottom),
        contentPadding = PaddingValues(vertical = 12.dp),
    ) {
        items(
            count = generatedImages.itemCount,
            key = generatedImages.itemKey { it.id },
            contentType = generatedImages.itemContentType { "INDICATOR_CONTENT_TYPE" },
        ) { index ->
            val isFocused = remember {
                derivedStateOf { pagerState.settledPage == index }
            }

            GenerationItem(
                focused = isFocused,
                imageUrl = generatedImages[index]?.imageUrl,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}

@Composable
private fun GenerationItem(
    modifier: Modifier = Modifier,
    focused: State<Boolean>,
    imageUrl: String?,
    onClick: () -> Unit,
) {
    val theme = LocalTheme.current
    val borderColor by animateColorAsState(
        targetValue = if (focused.value) {
            theme.color.onDark
        } else {
            theme.color.onDark.copy(alpha = 0.2f)
        },
        label = "border color",
    )

    val sharedShapeDp = 16.dp
    val sharedShape = RoundedCornerShape(sharedShapeDp)

    AiutaImage(
        modifier = modifier
            .height(108.dp)
            .width(54.dp)
            .clip(sharedShape)
            .border(
                width = 3.dp,
                color = borderColor,
                shape = sharedShape,
            )
            .clickableUnindicated(onClick = onClick),
        shapeDp = sharedShapeDp,
        imageUrl = imageUrl,
        contentScale = ContentScale.Crop,
        contentDescription = null,
    )
}
