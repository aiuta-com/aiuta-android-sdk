package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model

import com.aiuta.fashionsdk.configuration.mode.AiutaMode
import com.aiuta.fashionsdk.configuration.mode.shoes.AiutaShoesMode
import com.aiuta.fashionsdk.internal.navigation.controller.AiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnScreen

/**
 * Routes to the predefined-model selector matching the current [mode] — the centred-pager
 * [TryOnScreen.ModelSelector] for [AiutaMode.GENERAL] or the view-blocks
 * [TryOnScreen.ModelSelectorShoes] for [AiutaMode.SHOES].
 *
 * When [mode] is [AiutaMode.SHOES] but [shoesMode] is not configured ([shoesMode] is `null`),
 * falls back to the general [TryOnScreen.ModelSelector] so we never land on the shoes selector
 * without a backing shoes configuration.
 */
internal fun AiutaNavigationController.navigateToModelSelector(
    mode: AiutaMode,
    shoesMode: AiutaShoesMode?,
) {
    navigateTo(
        newScreen = when (mode) {
            AiutaMode.GENERAL -> TryOnScreen.ModelSelector
            AiutaMode.SHOES -> if (shoesMode != null) {
                TryOnScreen.ModelSelectorShoes
            } else {
                TryOnScreen.ModelSelector
            }
        },
    )
}
