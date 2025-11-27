package com.aiuta.fashionsdk.tryon.compose.domain.models

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem

/**
 * Configuration for product generation used by the try-on module.
 *
 * This immutable class describes the set of products that should be
 * generated or used when composing a virtual try-on scene.
 *
 * @property productsForGeneration list of child product that should be generated.
 * Should not be empty. Depends on size - will see Single Try-on or Multi-Try-on experience
 */
@Immutable
public class ProductConfiguration(
    public val productsForGeneration: List<ProductItem>,
) {
    init {
        check(productsForGeneration.isNotEmpty()) {
            "Product configuration must contain at least one product"
        }
    }
}
