package com.aiuta.fashionsdk.tryon.compose.data.internal.entity.remote.models

import com.aiuta.fashionsdk.tryon.core.data.datasource.image.models.AiutaFileType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TryOnModelDTO(
    @SerialName("id")
    val id: String,
    @SerialName("owner_type")
    val ownerType: AiutaFileType,
    @SerialName("url")
    val url: String,
    @SerialName("expires_at")
    val expiresAt: String? = null,
    @SerialName("tags")
    val tags: Map<String, String> = emptyMap(),
)
