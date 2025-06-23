package com.aiuta.fashionsdk.tryon.compose.domain.models.internal.zoom

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.SharedImageTransitionModel

@Immutable
internal class ZoomImageUiModel(
    override val imageSize: Size,
    override val initialCornerRadius: Dp,
    override val parentImageOffset: Offset,
    val imageUrl: String?,
    // Analytic
    val originPageId: AiutaAnalyticsPageId,
) : SharedImageTransitionModel {
    companion object {
        val EMPTY by lazy {
            ZoomImageUiModel(
                imageSize = Size.Unspecified,
                initialCornerRadius = 0.dp,
                imageUrl = null,
                parentImageOffset = Offset.Unspecified,
                originPageId = AiutaAnalyticsPageId.RESULTS,
            )
        }
    }
}
