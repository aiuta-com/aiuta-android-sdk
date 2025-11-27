package sample.tryon.kmp.utils

import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem
import com.aiuta.fashionsdk.tryon.compose.domain.models.ProductConfiguration
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationItem

fun buildSingleMockProductConfiguration(generationItem: ProductGenerationItem): ProductConfiguration = ProductConfiguration(
    productsForGeneration = listOf(generationItem.toMockProductItem()),
)

fun buildMultiMockProductConfiguration(generationItems: List<ProductGenerationItem>): ProductConfiguration = ProductConfiguration(
    productsForGeneration = generationItems.mapIndexed { index, item ->
        item.toMockProductItem(
            title = "MOCK №$index straight leg jeans in light blue",
            brand = "MOCK BRAND",
        )
    },
)

fun ProductGenerationItem.toMockProductItem(
    title: String? = null,
    brand: String? = null,
): ProductItem = ProductItem(
    id = productId,
    title = title ?: "MOCK 90s straight leg jeans in light blue",
    imageUrls = imageUrls,
    brand = brand ?: "MOCK BRAND",
)
