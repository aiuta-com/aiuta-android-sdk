package com.aiuta.fashionsdk.internal.navigation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.internal.navigation.AiutaNavEntry
import com.aiuta.fashionsdk.internal.navigation.AiutaNavigationScreen
import com.aiuta.fashionsdk.internal.navigation.composition.LocalNavigationController
import com.aiuta.fashionsdk.internal.navigation.solveTransitionAnimation
import com.aiuta.fashionsdk.internal.navigation.utils.leftToRightTransition

@Composable
internal fun NavigationContent(
    contentEntryProvider: (AiutaNavigationScreen) -> AiutaNavEntry<AiutaNavigationScreen>,
    modifier: Modifier = Modifier,
) {
    val navigationController = LocalNavigationController.current

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
                // Default
                // TODO solve position
                // screenPosition(initialState) < screenPosition(targetState) -> rightToLeftTransition
                else -> leftToRightTransition
            }
        },
        contentKey = { it.id },
    ) { targetScreen ->
        contentEntryProvider(targetScreen).Content()
    }
}
