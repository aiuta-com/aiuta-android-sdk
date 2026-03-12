package com.aiuta.fashionsdk.configuration.features.models.images

/**
 * Represents an input image in the SDK's image processing pipeline.
 *
 * This class encapsulates the essential information about an image that
 * will be processed by the SDK, including its identifier, URL, and ownership type.
 *
 * @property id Unique identifier for the image
 * @property url URL where the image can be accessed
 * @property ownerType Ownership ownerType of the image (user or Aiuta)
 * @see AiutaOwnerType
 */
public class AiutaInputImage(
    public val id: String,
    public val url: String,
    public val ownerType: AiutaOwnerType,
)
