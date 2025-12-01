package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.footer.blocks.common

import androidx.compose.material.BottomSheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.util.lerp
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.controller.GenerationResultController

@Composable
internal fun alphaForBottomSheetConnection(
    generationResultController: GenerationResultController,
): State<Float> {
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

    return remember(sheetProgress.value) {
        derivedStateOf {
            lerp(
                start = 0.1f,
                stop = 1f,
                fraction = sheetProgress.value,
            )
        }
    }
}
