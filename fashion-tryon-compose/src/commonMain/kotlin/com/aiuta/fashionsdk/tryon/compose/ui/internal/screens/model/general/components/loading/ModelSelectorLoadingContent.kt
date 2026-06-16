package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.general.components.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.general.components.list.CentredModelsHorizontalPager
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.offsetForPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.placeholderFadeConnecting
import kotlin.math.absoluteValue

@Composable
internal fun ModelSelectorLoadingContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .placeholderFadeConnecting(shapeDp = 0.dp),
        )

        Spacer(Modifier.height(16.dp))

        ModelsListShimmer(
            modifier = Modifier.fillMaxWidth(),
            horizontalPager = rememberPagerState { 10 },
        )

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun ModelsListShimmer(
    modifier: Modifier = Modifier,
    horizontalPager: PagerState,
) {
    CentredModelsHorizontalPager(
        modifier = modifier.height(124.dp),
        state = horizontalPager,
    ) { index ->
        val pageOffset =
            remember {
                derivedStateOf {
                    1 - horizontalPager.offsetForPage(index)
                        .absoluteValue
                        .coerceIn(
                            0f,
                            1f,
                        )
                }
            }

        val itemHeight =
            remember {
                derivedStateOf {
                    lerp(
                        97.dp,
                        114.dp,
                        pageOffset.value,
                    )
                }
            }

        val itemWidth =
            remember {
                derivedStateOf {
                    lerp(
                        61.dp,
                        70.dp,
                        pageOffset.value,
                    )
                }
            }

        Box(
            modifier =
            Modifier
                .width(itemWidth.value)
                .height(itemHeight.value)
                .placeholderFadeConnecting(shapeDp = 8.dp),
        )
    }
}
