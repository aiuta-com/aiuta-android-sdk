package com.aiuta.fashionsdk.internal.navigation.internal.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.savedstate.compose.LocalSavedStateRegistryOwner
import com.aiuta.fashionsdk.internal.navigation.AiutaNavEntry
import com.aiuta.fashionsdk.internal.navigation.AiutaNavigationScreen
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.internal.navigation.controller.AiutaNavigationDirection
import com.aiuta.fashionsdk.internal.navigation.internal.utils.leftToRightTransition
import com.aiuta.fashionsdk.internal.navigation.internal.utils.rightToLeftTransition
import com.aiuta.fashionsdk.internal.navigation.solveTransitionAnimation
import com.aiuta.fashionsdk.internal.navigation.viewmodel.NavigationViewModelStoreOwner
import com.aiuta.fashionsdk.internal.navigation.viewmodel.getNavigationViewModelStoreManager

@Composable
internal fun NavigationContent(
    contentEntryProvider: (AiutaNavigationScreen) -> AiutaNavEntry<AiutaNavigationScreen>,
    modifier: Modifier = Modifier,
) {
    val navigationController = LocalAiutaNavigationController.current

    // Get parent ViewModelStoreOwner (typically the Activity or Fragment)
    val parentViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner provided. Make sure NavigationContent is used within a ViewModelStoreOwner context."
    }
    // Get or create the NavigationViewModelStoreManager
    val viewModelStoreManager = remember(parentViewModelStoreOwner) {
        parentViewModelStoreOwner.viewModelStore.getNavigationViewModelStoreManager()
    }

    // Register cleanup callback with navigation controller
    DisposableEffect(navigationController, viewModelStoreManager) {
        navigationController.setViewModelStoreManager(viewModelStoreManager)
        onDispose {
            navigationController.setViewModelStoreManager(null)
        }
    }

    val transition = updateTransition(
        targetState = navigationController.currentScreen.value,
        label = "navigation transition",
    )

    transition.AnimatedContent(
        modifier = modifier,
        transitionSpec = {
            val destinationsTransition = solveTransitionAnimation()

            when {
                // Solve custom transition animation
                destinationsTransition != null -> destinationsTransition
                // Default based on navigation direction
                navigationController.aiutaNavigationDirection == AiutaNavigationDirection.Forward -> rightToLeftTransition
                else -> leftToRightTransition
            }
        },
        contentKey = { it.id },
    ) { targetScreen ->
        val entry = remember(targetScreen) {
            contentEntryProvider(targetScreen)
        }

        // Get SavedStateRegistryOwner for this screen
        val savedStateRegistryOwner = LocalSavedStateRegistryOwner.current

        // Create screen-scoped ViewModelStore and ViewModelStoreOwner
        val screenViewModelStore = remember(targetScreen.id) {
            viewModelStoreManager.getViewModelStoreForScreen(targetScreen.id)
        }
        val screenViewModelStoreOwner = remember(targetScreen.id, savedStateRegistryOwner) {
            NavigationViewModelStoreOwner(
                viewModelStore = screenViewModelStore,
                savedStateRegistryOwner = savedStateRegistryOwner,
            )
        }

        // Provide screen-scoped ViewModelStoreOwner
        CompositionLocalProvider(
            LocalViewModelStoreOwner provides screenViewModelStoreOwner,
        ) {
            entry.Content()
        }
    }
}
