package com.aiuta.fashionsdk.configuration.features.models.product
/**
 * Represents the price information for a product.
 *
 * @property current Current price of the product, formatted as a localized string including currency symbol and amount.
 * @property old Optional old price of the product, formatted as a localized string.
 */
public class ProductPrice(
    public val current: String,
    public val old: String? = null,
)
