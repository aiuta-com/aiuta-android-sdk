package com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images

import androidx.compose.runtime.Immutable

@Immutable
internal data class SessionImageUIModel(
    val id: String,
    val imageUrl: String,
    val isFeedbackProvided: Boolean = false,
    val productIds: List<String>,
)
