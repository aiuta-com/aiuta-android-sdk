package com.aiuta.fashionsdk.tryon.core.data.datasource.image.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UploadedImage(
    @SerialName("id")
    val id: String,
    @SerialName("url")
    val url: String,
    @SerialName("owner_type")
    val ownerType: AiutaFileType,
)
