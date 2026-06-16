package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.general.components.content

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.button.FashionButton
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonSizes
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonStyles
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.tryon.AiutaTryOnFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.general.models.ModelSelectorScreenEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.general.models.ModelSelectorScreenViewState

@Composable
internal fun ModelSelectorShowContent(
    viewState: State<ModelSelectorScreenViewState>,
    eventHandler: (ModelSelectorScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current

    val tryOnFeature = strictProvideFeature<AiutaTryOnFeature>()
    val state = viewState.value

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AiutaImage(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            imageUrl = state.activeModel?.url,
            shape = RoundedCornerShape(0.dp),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )

        Spacer(Modifier.height(16.dp))

        val activeGenderTransition = updateTransition(state.activeGender)
        activeGenderTransition.AnimatedContent(
            modifier = Modifier.fillMaxWidth(),
            contentKey = { it?.id },
        ) { gender ->
            gender?.let {
                ModelsListBlock(
                    models = it.models,
                    onActiveModelChanged = { model ->
                        eventHandler(ModelSelectorScreenEvent.ActiveModelChanged(model))
                    },
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        FashionButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = tryOnFeature.strings.tryOn,
            style = tryOnFeature.styles.tryOnButtonGradient?.let { tryOnButtonGradient ->
                FashionButtonStyles.gradientColors(
                    contentColor = theme.color.onDark,
                    gradientBackground = Brush.horizontalGradient(tryOnButtonGradient),
                )
            } ?: FashionButtonStyles.primaryStyle(theme),
            size = FashionButtonSizes.lSize(),
            icon = tryOnFeature.icons.tryOn20,
            onClick = {
                eventHandler(ModelSelectorScreenEvent.TryOnClicked)
            },
        )

        Spacer(Modifier.height(20.dp))
    }
}
