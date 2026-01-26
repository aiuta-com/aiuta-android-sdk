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
import com.aiuta.fashionsdk.internal.navigation.viewmodel.NavigationViewModelStoreManager

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

    internal var aiutaNavigationDirection: AiutaNavigationDirection =
        AiutaNavigationDirection.Forward
        private set

    // ViewModelStore manager for cleaning up ViewModels
    private var viewModelStoreManager: NavigationViewModelStoreManager? = null

    /**
     * Sets the ViewModelStoreManager for this controller.
     * This is called by NavigationContent to enable ViewModel cleanup.
     *
     * @param manager The NavigationViewModelStoreManager or null to clear
     */
    internal fun setViewModelStoreManager(manager: NavigationViewModelStoreManager?) {
        viewModelStoreManager = manager
    }

    public fun navigateTo(newScreen: AiutaNavigationScreen) {
        // Save previous screen, if we should not skip it in back stack
        if (currentScreen.value.shouldSaveInBackStack) {
            backStack.addLast(currentScreen.value)
        } else {
            // If screen is not saved in backstack, clear its ViewModelStore immediately
            viewModelStoreManager?.clearViewModelStoreForScreen(currentScreen.value.id)
        }

        // Set direction and new screen
        aiutaNavigationDirection = AiutaNavigationDirection.Forward
        currentScreen.value = newScreen
    }

    public fun popUpAndNavigateTo(
        navigateToScreen: AiutaNavigationScreen,
        popUpScreen: AiutaNavigationScreen? = null,
    ) {
        // Collect screen IDs that will be removed
        val removedScreenIds = mutableSetOf<String>()

        // Remove all screens including popUpScreen
        while (true) {
            if (backStack.isEmpty()) {
                break
            }

            val previousScreen = backStack.removeLast()
            removedScreenIds.add(previousScreen.id)

            if (popUpScreen == null || previousScreen == popUpScreen) {
                break
            }
        }

        // Clear ViewModelStores for removed screens
        viewModelStoreManager?.clearViewModelStoresForScreens(removedScreenIds)

        navigateTo(navigateToScreen)
    }

    public fun navigateBack() {
        aiutaNavigationDirection = AiutaNavigationDirection.Backward
        if (backStack.isNotEmpty()) {
            // Clear ViewModelStore for the current screen before navigating back
            val screenToRemove = currentScreen.value
            viewModelStoreManager?.clearViewModelStoreForScreen(screenToRemove.id)

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
            // Clear ViewModelStore for current screen
            viewModelStoreManager?.clearViewModelStoreForScreen(currentScreen.value.id)

            // Clear ViewModelStores for all screens in backstack
            val backStackScreenIds = backStack.map { it.id }.toSet()
            viewModelStoreManager?.clearViewModelStoresForScreens(backStackScreenIds)

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
