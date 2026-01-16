package com.aiuta.fashionsdk.internal.navigation.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.aiuta.fashionsdk.internal.navigation.AiutaNavigationScreen

@Composable
internal fun rememberAiutaNavigationController(
    startScreen: AiutaNavigationScreen,
    clickClose: () -> Unit,
): AiutaNavigationController = remember(
    startScreen,
    clickClose,
) {
    AiutaNavigationController(
        startScreen = startScreen,
        clickClose = clickClose,
    )
}

@Immutable
public class AiutaNavigationController internal constructor(
    startScreen: AiutaNavigationScreen,
    private val clickClose: () -> Unit,
) {
    private val backStack: ArrayDeque<AiutaNavigationScreen> = ArrayDeque()
    public val currentScreen: MutableState<AiutaNavigationScreen> = mutableStateOf(startScreen)

    fun navigateTo(
        newScreen: AiutaNavigationScreen,
        shouldSaveCurrentScreen: Boolean = true,
    ) {
        // Save previous screen, if we should not skip it in back stack
        if (shouldSaveCurrentScreen) {
            backStack.addLast(currentScreen.value)
        }

        // Set new screen
        currentScreen.value = newScreen
    }

    fun popUpAndNavigateTo(
        navigateToScreen: AiutaNavigationScreen,
        popUpScreen: AiutaNavigationScreen? = null,
    ) {
        // Remove all screens including popUpScreen
        while (true) {
            if (backStack.isEmpty()) {
                break
            }

            val previousScreen = backStack.removeLast()
            if (popUpScreen == null || previousScreen == popUpScreen) {
                break
            }
        }

        navigateTo(navigateToScreen)
    }

    fun navigateBack() {
        if (backStack.isNotEmpty()) {
            val previousScreen = backStack.removeLast()

            currentScreen.value = previousScreen
        } else {
            clickClose()
        }
    }
}
