package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.list.components.navigation

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.GeneratedImageUIModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.SessionImageUIModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.list.utils.fadingEdge
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.offsetForPage
import kotlin.math.absoluteValue
import kotlinx.coroutines.launch

private val ITEM_HEIGHT = 64.dp

@Composable
internal fun GenerationIndicator(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    generatedImages: LazyPagingItems<GeneratedImageUIModel>,
) {
    GenerationIndicatorCore(
        modifier = modifier,
        pagerState = pagerState,
        itemCount = generatedImages.itemCount,
        key = { index -> generatedImages[index]?.id ?: index },
        imageUrl = { index -> generatedImages[index]?.imageUrl },
    )
}

@Composable
internal fun GenerationIndicator(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    generations: List<SessionImageUIModel>,
    topPadding: Dp = 0.dp,
) {
    GenerationIndicatorCore(
        modifier = modifier,
        pagerState = pagerState,
        itemCount = generations.size,
        key = { index -> generations.getOrNull(index)?.id ?: index },
        imageUrl = { index -> generations.getOrNull(index)?.imageUrl },
        topPadding = topPadding,
    )
}

@Composable
private fun GenerationIndicatorCore(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    itemCount: Int,
    key: (Int) -> Any,
    imageUrl: (Int) -> String?,
    topPadding: Dp = 52.dp, // app bar padding
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

    val prevState = remember { mutableFloatStateOf(0f) }
    val heightDp = with(LocalDensity.current) { ITEM_HEIGHT.toPx() }

    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPageOffsetFraction + pagerState.currentPage }
            .collect { offset ->
                val delta = heightDp * (offset - prevState.value)
                indicatorState.scrollBy(delta)
                prevState.value = offset
            }
    }

    LazyColumn(
        modifier = modifier
            .padding(top = topPadding)
            .fadingEdge(brush = fadeBrush),
        state = indicatorState,
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.Top),
        contentPadding = PaddingValues(vertical = 16.dp),
    ) {
        items(
            count = itemCount,
            key = { index -> key(index) },
            contentType = { "INDICATOR_CONTENT_TYPE" },
        ) { index ->
            val pageOffset = remember { derivedStateOf { pagerState.offsetForPage(index) } }

            GenerationItem(
                pageOffset = pageOffset,
                imageUrl = imageUrl(index),
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
    pageOffset: State<Float>,
    imageUrl: String?,
    onClick: () -> Unit,
) {
    val theme = LocalTheme.current
    val borderColor = lerp(
        start = theme.color.onDark,
        stop = theme.color.onDark.copy(alpha = 0.2f),
        fraction = pageOffset.value.absoluteValue.coerceIn(0f, 1f),
    )

    val sharedShapeDp = 16.dp
    val sharedShape = RoundedCornerShape(sharedShapeDp)

    AiutaImage(
        modifier = modifier
            .height(ITEM_HEIGHT)
            .width(44.dp)
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
