package com.aiuta.fashionsdk.tryon.compose.domain.models.internal.config

import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.config.features.FeedbackFeature
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.config.features.FitDisclaimerFeature
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.config.features.PoweredByStickerFeature
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class ClientConfiguration(
    @SerialName("powered_by_sticker")
    val poweredByStickerFeature: PoweredByStickerFeature? = null,
    @SerialName("feedback")
    val feedbackFeature: FeedbackFeature? = null,
    @SerialName("size_and_fit_disclaimer")
    val fitDisclaimerFeature: FitDisclaimerFeature? = null,
)
