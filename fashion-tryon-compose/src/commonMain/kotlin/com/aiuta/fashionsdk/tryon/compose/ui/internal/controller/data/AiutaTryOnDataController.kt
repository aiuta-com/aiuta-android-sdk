package com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.internal.subscription.InternalAiutaSubscription
import com.aiuta.fashionsdk.internal.subscription.internalAiutaSubscription
import com.aiuta.fashionsdk.tryon.compose.data.internal.repository.models.TryOnModelsRepository

@Composable
internal fun rememberAiutaTryOnDataController(aiuta: () -> Aiuta): AiutaTryOnDataController = remember {
    AiutaTryOnDataController(
        tryOnModelsRepository = TryOnModelsRepository.getInstance(aiuta()),
        subscription = aiuta().internalAiutaSubscription,
    )
}

@Immutable
internal class AiutaTryOnDataController(
    val tryOnModelsRepository: TryOnModelsRepository,
    val subscription: InternalAiutaSubscription,
)
