package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.content

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.tryon.AiutaTryOnFeature
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.isNotEmpty
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.screen.selector.ImageSelectorScreenState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.appbar.MainAppBar
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.content.empty.ImageSelectorScreenEmptyContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.content.nonempty.ImageSelectorScreenNonEmptyContent

@Composable
internal fun ImageSelectorScreenContent(modifier: Modifier = Modifier) {
    val controller = LocalController.current
    val theme = LocalTheme.current

    val tryOnFeature = strictProvideFeature<AiutaTryOnFeature>()

    val screenState =
        remember {
            derivedStateOf {
                when {
                    controller.lastSavedImages.value.isNotEmpty() -> ImageSelectorScreenState.NON_EMPTY_SCREEN
                    else -> ImageSelectorScreenState.EMPTY_SCREEN
                }
            }
        }
    val screenStateTransition = updateTransition(screenState.value)

    Column(
        modifier = modifier
            .background(theme.color.background)
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MainAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            title = tryOnFeature.strings.tryOnPageTitle,
        )

        Spacer(Modifier.height(16.dp))

        screenStateTransition.AnimatedContent(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) { state ->
            when (state) {
                ImageSelectorScreenState.EMPTY_SCREEN -> {
                    ImageSelectorScreenEmptyContent(modifier = Modifier.fillMaxSize())
                }

                ImageSelectorScreenState.NON_EMPTY_SCREEN -> {
                    ImageSelectorScreenNonEmptyContent(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}
