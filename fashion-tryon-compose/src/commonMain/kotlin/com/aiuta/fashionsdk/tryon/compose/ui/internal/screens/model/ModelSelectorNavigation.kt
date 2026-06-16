package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model

import com.aiuta.fashionsdk.configuration.mode.AiutaMode
import com.aiuta.fashionsdk.internal.navigation.controller.AiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnScreen

/**
 * Routes to the predefined-model selector matching the current [mode] — the centred-pager
 * [TryOnScreen.ModelSelector] for [AiutaMode.GENERAL] or the view-blocks
 * [TryOnScreen.ModelSelectorShoes] for [AiutaMode.SHOES].
 */
internal fun AiutaNavigationController.navigateToModelSelector(mode: AiutaMode) {
    navigateTo(
        newScreen = when (mode) {
            AiutaMode.GENERAL -> TryOnScreen.ModelSelector
            AiutaMode.SHOES -> TryOnScreen.ModelSelectorShoes
        },
    )
}
