package com.aiuta.fashionsdk.tryon.compose.data.internal.entity.remote.models

import kotlinx.serialization.Serializable

@Serializable
internal data class CachedTryOnModels(
    val etag: String? = null,
    val models: List<TryOnModelDTO> = emptyList(),
)
