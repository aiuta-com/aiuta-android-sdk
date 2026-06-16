package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel.TryOnModelUiModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.list.CentredModelsHorizontalPager
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.offsetForPage
import kotlin.math.absoluteValue

/**
 * GENERAL mode content — a single centred horizontal pager over a flat list of models.
 * The settled page drives the active model via [onActiveModelChanged].
 */
@Composable
internal fun ModelsListBlock(
    models: List<TryOnModelUiModel>,
    onActiveModelChanged: (TryOnModelUiModel?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sharedShape = RoundedCornerShape(8.dp)

    val horizontalPager = rememberPagerState { models.size }

    LaunchedEffect(horizontalPager.settledPage) {
        onActiveModelChanged(models.getOrNull(horizontalPager.settledPage))
    }

    CentredModelsHorizontalPager(
        modifier = modifier.height(124.dp),
        state = horizontalPager,
    ) { index ->
        val pageOffset = remember {
            derivedStateOf {
                1 -
                    horizontalPager.offsetForPage(
                        index,
                    ).absoluteValue.coerceIn(
                        0f,
                        1f,
                    )
            }
        }

        val itemHeight = remember {
            derivedStateOf {
                lerp(
                    105.dp,
                    124.dp,
                    pageOffset.value,
                )
            }
        }

        val itemWidth = remember {
            derivedStateOf {
                lerp(
                    66.dp,
                    76.dp,
                    pageOffset.value,
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            AiutaImage(
                modifier = Modifier
                    .width(itemWidth.value)
                    .height(itemHeight.value)
                    .clip(sharedShape),
                imageUrl = models.getOrNull(index)?.url,
                shape = sharedShape,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
    }
}
