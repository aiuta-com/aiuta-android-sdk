package com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.remote.models.TryOnModelDTO
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.UrlImage
import com.aiuta.fashionsdk.tryon.core.data.datasource.image.models.AiutaFileType

@Immutable
internal data class TryOnModelUiModel(
    val id: String,
    val url: String,
    val type: AiutaFileType,
    val tags: Map<String, String>,
)

internal fun TryOnModelDTO.toUiModel(): TryOnModelUiModel = TryOnModelUiModel(
    id = id,
    url = url,
    type = ownerType,
    tags = tags,
)

internal fun TryOnModelUiModel.toUrlImage(): UrlImage = UrlImage(
    imageId = id,
    imageUrl = url,
    imageType = type,
)
