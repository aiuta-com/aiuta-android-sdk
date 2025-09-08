package sample.tryon.kmp.web.blocks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationItem

@Composable
internal fun ActiveProductBlock(
    modifier: Modifier = Modifier,
    pickedProduct: State<ProductGenerationItem?>,
) {
    val isVisible = remember {
        derivedStateOf {
            pickedProduct.value != null
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible.value,
    ) {
        pickedProduct.value?.let { product ->
            ActiveProductBlockContent(
                modifier = Modifier.fillMaxWidth(),
                product = product,
            )
        }
    }
}

@Composable
private fun ActiveProductBlockContent(
    modifier: Modifier = Modifier,
    product: ProductGenerationItem,
) {
    val sharedShape = RoundedCornerShape(24.dp)

    Row(
        modifier = modifier
            .background(
                color = Color.White,
                shape = sharedShape,
            )
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = sharedShape,
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            modifier = Modifier
                .width(50.dp)
                .aspectRatio(0.5f)
                .clip(RoundedCornerShape(16.dp)),
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(product.imageUrls.firstOrNull())
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        Spacer(Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            product.catalogName?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                )

                Spacer(Modifier.height(8.dp))
            }

            Text(
                text = product.title,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
