package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.footer.blocks.common

import androidx.compose.material.BottomSheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.controller.GenerationResultController

@Composable
internal fun Modifier.foregroundForBottomSheetConnection(
    generationResultController: GenerationResultController,
): Modifier {
    val bottomSheetState = generationResultController
        .bottomSheetScaffoldState
        .bottomSheetState
    val sheetProgress = remember(bottomSheetState) {
        derivedStateOf {
            bottomSheetState.progress(
                from = BottomSheetValue.Collapsed,
                to = BottomSheetValue.Expanded,
            )
        }
    }

    val gradientAlpha = remember {
        derivedStateOf {
            (1f - sheetProgress.value).coerceIn(0f, 1f)
        }
    }

    return drawWithContent {
        drawContent()
        drawRect(
            brush =
            Brush.verticalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.4f * gradientAlpha.value),
                    Color.White.copy(alpha = 0.9f * gradientAlpha.value),
                ),
            ),
        )
    }
}
