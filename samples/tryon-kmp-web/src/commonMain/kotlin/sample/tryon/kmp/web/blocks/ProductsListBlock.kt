package sample.tryon.kmp.web.blocks

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationItem

@Composable
internal fun ProductsListBlock(
    modifier: Modifier,
    products: List<ProductGenerationItem>,
    onProductClick: (ProductGenerationItem) -> Unit,
) {
    val sharedShape = RoundedCornerShape(16.dp)

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(100.dp),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 8.dp,
        ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = products,
            key = { product -> product.productId },
            contentType = { "PRODUCT_CONTENT_TYPE" },
        ) { product ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.5f)
                    .clip(sharedShape)
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = sharedShape,
                    )
                    .clickable {
                        onProductClick(product)
                    }
                    .padding(8.dp),
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(sharedShape),
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(product.imageUrls.firstOrNull())
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp),
                    text = product.title,
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
