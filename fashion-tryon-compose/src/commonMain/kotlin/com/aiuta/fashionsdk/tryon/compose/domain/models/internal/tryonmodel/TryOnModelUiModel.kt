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
    val tags: TryOnModelTags,
)

/**
 * Projects a DTO into the UI model, parsing its raw `tags` into a typed [TryOnModelTags]. Returns
 * `null` when `gender`/`view` is missing or holds an unrecognized value — such models are simply
 * not shown.
 */
internal fun TryOnModelDTO.toUiModel(): TryOnModelUiModel? {
    val tags = tags.toTryOnModelTags() ?: return null
    return TryOnModelUiModel(
        id = id,
        url = url,
        type = ownerType,
        tags = tags,
    )
}

internal fun TryOnModelUiModel.toUrlImage(): UrlImage = UrlImage(
    imageId = id,
    imageUrl = url,
    imageType = type,
)
