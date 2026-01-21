package com.aiuta.fashionsdk.internal.navigation.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsExitEvent
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.internal.analytics.internalAiutaAnalytic
import com.aiuta.fashionsdk.internal.navigation.AiutaNavigationScreen

@Composable
internal fun rememberAiutaNavigationController(
    startScreen: AiutaNavigationScreen,
    aiutaConfiguration: AiutaConfiguration,
): AiutaNavigationController = remember(
    startScreen,
    aiutaConfiguration,
) {
    AiutaNavigationController(
        startScreen = startScreen,
        aiutaConfiguration = aiutaConfiguration,
    )
}

@Immutable
public class AiutaNavigationController internal constructor(
    startScreen: AiutaNavigationScreen,
    private val aiutaConfiguration: AiutaConfiguration,
) {
    internal val analytic by lazy { aiutaConfiguration.aiuta.internalAiutaAnalytic }

    private val backStack: ArrayDeque<AiutaNavigationScreen> = ArrayDeque()
    public val currentScreen: MutableState<AiutaNavigationScreen> = mutableStateOf(startScreen)

    internal var aiutaNavigationDirection: AiutaNavigationDirection = AiutaNavigationDirection.Forward
        private set

    public fun navigateTo(newScreen: AiutaNavigationScreen) {
        // Save previous screen, if we should not skip it in back stack
        if (currentScreen.value.shouldSaveInBackStack) {
            backStack.addLast(currentScreen.value)
        }

        // Set direction and new screen
        aiutaNavigationDirection = AiutaNavigationDirection.Forward
        currentScreen.value = newScreen
    }

    public fun popUpAndNavigateTo(
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

    public fun navigateBack() {
        aiutaNavigationDirection = AiutaNavigationDirection.Backward
        if (backStack.isNotEmpty()) {
            val previousScreen = backStack.removeLast()
            currentScreen.value = previousScreen
        } else {
            clickClose()
        }
    }

    public fun clickClose(
        pageId: AiutaAnalyticsPageId? = null,
        productIds: List<String> = emptyList(),
    ) {
        runCatching {
            analytic.sendEvent(
                event = AiutaAnalyticsExitEvent(
                    pageId = pageId ?: currentScreen.value.exitPageId,
                    productIds = productIds,
                ),
            )
            aiutaConfiguration.userInterface.actions.closeClick()
        }
    }
}
