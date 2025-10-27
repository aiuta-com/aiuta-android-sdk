package com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.tryon.compose.data.internal.repository.config.ConfigRepository
import com.aiuta.fashionsdk.tryon.compose.data.internal.repository.subscription.SubscriptionDetailsRepository

@Composable
internal fun rememberAiutaTryOnDataController(aiuta: () -> Aiuta): AiutaTryOnDataController = remember {
    AiutaTryOnDataController(
        configRepository = ConfigRepository.getInstance(aiuta()),
        subscriptionDetailsRepository = SubscriptionDetailsRepository.getInstance(aiuta()),
    )
}

@Immutable
internal class AiutaTryOnDataController(
    val configRepository: ConfigRepository,
    val subscriptionDetailsRepository: SubscriptionDetailsRepository,
)
