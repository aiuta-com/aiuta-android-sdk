package com.aiuta.fashionsdk.configuration.features.models.product

import androidx.compose.runtime.Immutable

/**
 * Represents a product item in the SDK's product catalog.
 *
 * This immutable data class encapsulates all essential information about a product,
 * including its identification, description, images, pricing, and store information.
 * It is used throughout the SDK for product display and try-on generation.
 *
 * @property id Unique identifier for the product
 * @property title Name of the product
 * @property brand Brand associated with the product
 * @property imageUrls List of URLs to product images
 * @property price Price information for the product, if available
 */
@Immutable
public class ProductItem(
    public val id: String,
    public val title: String,
    public val brand: String,
    public val imageUrls: List<String>,
    public val price: ProductPrice? = null,
)
