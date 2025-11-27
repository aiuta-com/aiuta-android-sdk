package com.aiuta.fashionsdk.tryon.core.domain.models

import com.aiuta.fashionsdk.tryon.core.data.datasource.image.models.AiutaFileType
import com.aiuta.fashionsdk.tryon.core.data.datasource.operation.models.GeneratedImage

/**
 * Model of generated image after ping operations
 */
public class ProductGeneratedImage(
    public val id: String,
    public val url: String,
    public val type: AiutaFileType,
    public val productIds: List<String>,
)

internal fun GeneratedImage.toPublic(
    container: ProductGenerationContainer,
): ProductGeneratedImage = ProductGeneratedImage(
    id = id,
    url = url,
    type = type,
    productIds = container.productIds,
)
