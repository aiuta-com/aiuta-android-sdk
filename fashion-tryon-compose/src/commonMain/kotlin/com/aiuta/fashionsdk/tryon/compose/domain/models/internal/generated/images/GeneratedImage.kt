package com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.configuration.features.models.images.AiutaGeneratedImage
import com.aiuta.fashionsdk.configuration.features.models.images.AiutaOwnerType
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.local.generated.images.GeneratedImageEntity
import com.aiuta.fashionsdk.tryon.core.data.datasource.image.models.AiutaFileType
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGeneratedImage

@Immutable
internal data class GeneratedImageUIModel(
    val id: String,
    val imageUrl: String,
    val type: AiutaFileType,
    val productIds: List<String> = emptyList(),
)

internal fun GeneratedImageEntity.toUiModel(): GeneratedImageUIModel = GeneratedImageUIModel(
    id = id,
    imageUrl = imageUrl,
    type = type,
    productIds = productIds,
)

internal fun GeneratedImageUIModel.toEntity(): GeneratedImageEntity = GeneratedImageEntity(
    id = id,
    imageUrl = imageUrl,
    type = type,
    productIds = productIds,
)

internal fun GeneratedImageUIModel.toSessionUiModel(): SessionImageUIModel = SessionImageUIModel(
    id = id,
    imageUrl = imageUrl,
    productIds = productIds,
)

internal fun ProductGeneratedImage.toUiModel(): GeneratedImageUIModel = GeneratedImageUIModel(
    id = id,
    imageUrl = url,
    type = type,
    productIds = productIds,
)

// History image
internal fun GeneratedImageUIModel.toPublic(): AiutaGeneratedImage = AiutaGeneratedImage(
    id = id,
    url = imageUrl,
    type = type.toPublicHistory(),
    productIds = productIds,
)

internal fun AiutaGeneratedImage.toImageUiModel(): GeneratedImageUIModel = GeneratedImageUIModel(
    id = id,
    imageUrl = url,
    type = type.toPublicCore(),
)

// Type
internal fun AiutaOwnerType.toPublicCore(): AiutaFileType = when (this) {
    AiutaOwnerType.USER -> AiutaFileType.USER
    AiutaOwnerType.AIUTA -> AiutaFileType.AIUTA
}

internal fun AiutaFileType.toPublicHistory(): AiutaOwnerType = when (this) {
    AiutaFileType.USER -> AiutaOwnerType.USER
    AiutaFileType.AIUTA -> AiutaOwnerType.AIUTA
}
