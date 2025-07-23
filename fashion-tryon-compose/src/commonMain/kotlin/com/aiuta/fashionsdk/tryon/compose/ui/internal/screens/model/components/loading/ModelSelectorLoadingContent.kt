package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.aiuta.fashionsdk.compose.core.size.rememberScreenSize
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.list.CentredModelsHorizontalPager
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.utils.MODEL_IMAGE_BOTTOM_PADDING_COEF
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.utils.MODEL_IMAGE_HORIZONTAL_PADDING_COEF
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.offsetForPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.placeholderFadeConnecting
import kotlin.math.absoluteValue

@Composable
internal fun ModelSelectorLoadingContent(modifier: Modifier = Modifier) {
    val screenSize = rememberScreenSize()

    val imageHorizontalPadding = screenSize.widthDp * MODEL_IMAGE_HORIZONTAL_PADDING_COEF
    val bottomPadding = screenSize.heightDp * MODEL_IMAGE_BOTTOM_PADDING_COEF

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = imageHorizontalPadding)
                .placeholderFadeConnecting(shapeDp = 24.dp),
        )

        Spacer(Modifier.height(26.dp))

        SelectorShimmer()

        Spacer(Modifier.height(30.dp))

        ModelsListShimmer(
            modifier = Modifier.fillMaxWidth(),
            horizontalPager = rememberPagerState { 10 },
        )

        Spacer(Modifier.height(bottomPadding))
    }
}

@Composable
private fun SelectorShimmer(modifier: Modifier = Modifier) {
    val sharedModifier =
        Modifier
            .width(56.dp)
            .height(15.dp)
            .placeholderFadeConnecting(shapeDp = 4.dp)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Box(modifier = sharedModifier)

        Box(modifier = sharedModifier)
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
                    1 -
                        horizontalPager.offsetForPage(index).absoluteValue.coerceIn(
                            0f,
                            1f,
                        )
                }
            }

        val itemHeight =
            remember {
                derivedStateOf {
                    lerp(
                        105.dp,
                        124.dp,
                        pageOffset.value,
                    )
                }
            }

        val itemWidth =
            remember {
                derivedStateOf {
                    lerp(
                        66.dp,
                        76.dp,
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
