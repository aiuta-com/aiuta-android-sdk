package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.compose.core.size.rememberScreenSize
import com.aiuta.fashionsdk.configuration.features.tryon.AiutaTryOnFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendPageEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.appbar.MainAppBar
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.body.GenerationResultBody
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.common.ThanksFeedbackBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.footer.DisclaimerBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.footer.GenerationResultFooterList
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.controller.GenerationResultController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.controller.GenerationResultListener
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.controller.rememberGenerationResultController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.MAIN_IMAGE_SIZE
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.strictProvideFeature
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.uikit.utils.clickableUnindicated

@Composable
internal fun GenerationResultScreen(modifier: Modifier = Modifier) {
    sendPageEvent(pageId = AiutaAnalyticsPageId.RESULTS)

    GenerationResultListener()

    GenerationResultScreenContent(modifier = modifier)
}

@Composable
private fun GenerationResultScreenContent(modifier: Modifier = Modifier) {
    val screenSize = rememberScreenSize()
    val theme = LocalTheme.current

    val generationResultController = rememberGenerationResultController()

    val screenHeight = screenSize.heightDp

    val imageHeight = screenHeight * MAIN_IMAGE_SIZE
    val disclaimerHeight = 25.dp
    val sheetHeight = screenHeight - 8.dp - imageHeight - 32.dp - disclaimerHeight

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = generationResultController.bottomSheetScaffoldState,
        sheetContent = {
            GenerationResultFooterList(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                generationResultController = generationResultController,
            )
        },
        sheetBackgroundColor = theme.color.background,
        backgroundColor = theme.color.neutral,
        sheetShape = theme.bottomSheet.shapes.bottomSheetShape,
        sheetPeekHeight = sheetHeight,
    ) { paddings ->
        Box(
            modifier = Modifier
                .padding(paddings)
                .background(theme.color.background)
                .windowInsetsPadding(WindowInsets.navigationBars),
        ) {
            BottomSheetScaffoldContent(
                modifier = Modifier.fillMaxSize(),
                generationResultController = generationResultController,
            )

            ThanksFeedbackBlock(
                modifier = Modifier.align(Alignment.Center),
                generationResultController = generationResultController,
            )

            BottomSheetScaffoldScrim(
                modifier = Modifier.fillMaxSize(),
                generationResultController = generationResultController,
            )
        }
    }
}

@Composable
private fun BottomSheetScaffoldContent(
    modifier: Modifier = Modifier,
    generationResultController: GenerationResultController,
) {
    val tryOnFeature = strictProvideFeature<AiutaTryOnFeature>()

    Column(
        modifier = modifier,
    ) {
        MainAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            title = tryOnFeature.strings.tryOnPageTitle,
        )

        Spacer(Modifier.height(16.dp))

        GenerationResultBody(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            generationResultController = generationResultController,
        )

        Spacer(Modifier.height(32.dp))

        DisclaimerBlock(
            modifier = Modifier
                .height(25.dp)
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun BottomSheetScaffoldScrim(
    modifier: Modifier = Modifier,
    generationResultController: GenerationResultController,
) {
    val theme = LocalTheme.current

    val bottomSheetState =
        generationResultController
            .bottomSheetScaffoldState
            .bottomSheetState
    val sheetProgress =
        bottomSheetState.progress(
            from = BottomSheetValue.Collapsed,
            to = BottomSheetValue.Expanded,
        )
    val scrimColor =
        lerp(
            start = Color.Transparent,
            stop = theme.color.primary.copy(alpha = 0.6f),
            fraction = sheetProgress,
        )

    val isClicable =
        remember(sheetProgress) {
            derivedStateOf {
                sheetProgress == 1f
            }
        }

    val finalModifier =
        if (isClicable.value) {
            modifier
                .background(scrimColor)
                .clickableUnindicated { }
        } else {
            modifier.background(scrimColor)
        }

    Box(modifier = finalModifier)
}
