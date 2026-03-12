package com.aiuta.fashionsdk.configuration.features.models.images

/**
 * Represents an generated image in the SDK.
 *
 * This class encapsulates information about an image that has been generated
 * by the AI system, including its identifier, URL, ownership type, and associated
 * product identifiers.
 *
 * @property id Unique identifier for the generated image
 * @property url URL where the generated image can be accessed
 * @property ownerType Ownership ownerType of the image (user or Aiuta)
 * @property productIds List of product identifiers associated with this generated image
 * @see AiutaOwnerType
 */
public class AiutaGeneratedImage(
    public val id: String,
    public val url: String,
    public val ownerType: AiutaOwnerType,
    public val productIds: List<String>,
)
