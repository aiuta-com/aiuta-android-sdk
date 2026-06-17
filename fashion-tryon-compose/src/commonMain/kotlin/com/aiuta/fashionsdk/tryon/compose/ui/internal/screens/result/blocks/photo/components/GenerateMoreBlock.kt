package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.blocks.photo.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.compose.uikit.utils.provideFeature
import com.aiuta.fashionsdk.configuration.features.tryon.other.AiutaTryOnWithOtherPhotoFeature
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.SessionImageUIModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models.GenerationResultScreenEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models.GenerationResultScreenViewState
import dev.chrisbanes.haze.HazeState

@Composable
internal fun GenerateMoreBlock(
    state: GenerationResultScreenViewState,
    sessionImage: SessionImageUIModel,
    hazeState: HazeState,
    eventHandler: (GenerationResultScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (!state.isGenerateMoreAvailable) return
    val repickingFeature = provideFeature<AiutaTryOnWithOtherPhotoFeature>() ?: return

    ReactionIcon(
        modifier = modifier,
        icon = repickingFeature.icons.changePhoto24,
        hazeState = hazeState,
        onClick = {
            eventHandler(GenerationResultScreenEvent.ChangePhotoClicked(sessionImage))
        },
    )
}
