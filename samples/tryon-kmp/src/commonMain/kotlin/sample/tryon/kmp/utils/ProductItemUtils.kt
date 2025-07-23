package sample.tryon.kmp.utils

import androidx.compose.runtime.Composable
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationItem

@Composable
fun buildMockProductItem(generationItem: ProductGenerationItem): ProductItem = ProductItem(
    id = generationItem.productId,
    title = "MOCK 90s straight leg jeans in light blue",
    imageUrls = generationItem.imageUrls,
    brand = "MOCK BRAND",
)
