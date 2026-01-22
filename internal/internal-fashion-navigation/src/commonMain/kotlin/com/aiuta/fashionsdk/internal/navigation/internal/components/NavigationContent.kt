package com.aiuta.fashionsdk.internal.navigation.internal.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.internal.navigation.AiutaNavEntry
import com.aiuta.fashionsdk.internal.navigation.AiutaNavigationScreen
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.internal.navigation.controller.AiutaNavigationDirection
import com.aiuta.fashionsdk.internal.navigation.internal.utils.leftToRightTransition
import com.aiuta.fashionsdk.internal.navigation.internal.utils.rightToLeftTransition
import com.aiuta.fashionsdk.internal.navigation.solveTransitionAnimation

@Composable
internal fun NavigationContent(
    contentEntryProvider: (AiutaNavigationScreen) -> AiutaNavEntry<AiutaNavigationScreen>,
    modifier: Modifier = Modifier,
) {
    val navigationController = LocalAiutaNavigationController.current

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

        entry.Content()
    }
}
