package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.spalsh.models

import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnScreen

internal sealed interface SplashScreenAction {
    data class NavigateTo(val screen: TryOnScreen) : SplashScreenAction
}
