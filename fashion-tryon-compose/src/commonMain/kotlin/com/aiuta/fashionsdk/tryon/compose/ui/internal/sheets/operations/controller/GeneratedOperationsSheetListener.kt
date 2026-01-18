package com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.operations.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaBottomSheetNavigator
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.updateActiveOperationOrSetEmpty

@Composable
internal fun GeneratedOperationsSheetListener() {
    val bottomSheetNavigator = LocalAiutaBottomSheetNavigator.current
    val controller = LocalController.current

    LaunchedEffect(Unit) {
        controller.generatedOperationInteractor
            .countGeneratedOperation()
            .collect { countGeneratedOperation ->
                if (countGeneratedOperation == 0) {
                    controller.updateActiveOperationOrSetEmpty(null)
                    bottomSheetNavigator.hide()
                }
            }
    }
}
