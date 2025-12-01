package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.components.footer

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.components.footer.active.ActiveFooter
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.components.footer.inactive.InactiveFooter

@Composable
internal fun ImageSelectorFooter(
    modifier: Modifier = Modifier,
    isTryOnButtonVisible: State<Boolean>,
) {
    val footerTransition = updateTransition(isTryOnButtonVisible.value)

    footerTransition.AnimatedContent(
        modifier = modifier,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
    ) { isTryOnVisible ->
        if (isTryOnVisible) {
            ActiveFooter(modifier = Modifier.fillMaxSize())
        } else {
            InactiveFooter(modifier = Modifier.fillMaxSize())
        }
    }
}
