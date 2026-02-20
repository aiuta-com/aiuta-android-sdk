package sample.tryon.kmp.web.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationItem
import sample.tryon.kmp.web.AiutaViewModel

@Composable
internal fun SuccessScreen(
    modifier: Modifier = Modifier,
    products: List<ProductGenerationItem>,
    viewModel: AiutaViewModel,
) {
    val pickedProduct = remember { mutableStateOf<ProductGenerationItem?>(null) }

    Row(
        modifier = modifier.fillMaxSize(),
    ) {
        val sharedModifier = Modifier.fillMaxHeight().weight(1f)

        Box(
            modifier = sharedModifier,
        ) {
            ProductsListBlock(
                modifier = Modifier.fillMaxSize(),
                products = products,
                onProductClick = { product ->
                    pickedProduct.value = product
                },
            )

            ActiveProductBlock(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.6f)
                    .padding(bottom = 16.dp),
                pickedProduct = pickedProduct,
            )
        }

        Divider(Modifier.fillMaxHeight().width(1.dp).background(Color.Black))

        GenerationBlock(
            modifier = sharedModifier,
            pickedProduct = pickedProduct,
            viewModel = viewModel,
        )
    }
}
