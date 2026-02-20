package sample.tryon.kmp.web.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationItem
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationStatus
import sample.tryon.kmp.web.AiutaViewModel

@Composable
internal fun GenerationBlock(
    modifier: Modifier = Modifier,
    pickedProduct: State<ProductGenerationItem?>,
    viewModel: AiutaViewModel,
) {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val platformContext = LocalPlatformContext.current

    val windowHeight = with(density) { windowInfo.containerSize.height.toDp() }
    val innerVerticalPadding = windowHeight * 0.1f

    val generationState = viewModel.generationState.collectAsState()

    val isGenerationAvailable = remember(
        pickedProduct.value,
        generationState.value,
    ) {
        derivedStateOf {
            pickedProduct.value != null && generationState.value !is ProductGenerationStatus.LoadingGenerationStatus
        }
    }

    val isGenerationLoading = remember {
        derivedStateOf {
            generationState.value is ProductGenerationStatus.LoadingGenerationStatus
        }
    }

    val activeImageUrl = remember(
        generationState.value,
    ) {
        derivedStateOf {
            (generationState.value as? ProductGenerationStatus.SuccessGenerationStatus)
                ?.images
                ?.firstOrNull()
                ?.url
                ?: AiutaViewModel.TRYON_IMAGE_URL
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(innerVerticalPadding))

        Box(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(0.5f),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp)),
                model = ImageRequest.Builder(platformContext)
                    .data(activeImageUrl.value)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            androidx.compose.animation.AnimatedVisibility(
                visible = isGenerationLoading.value,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(32.dp),
                    color = MaterialTheme.colors.secondary,
                    backgroundColor = MaterialTheme.colors.surface,
                )
            }
        }

        Spacer(Modifier.height(innerVerticalPadding))

        Button(
            modifier = Modifier.fillMaxWidth(0.6f),
            enabled = isGenerationAvailable.value,
            onClick = {
                pickedProduct.value?.let { product ->
                    viewModel.startTryOn(product = product)
                }
            },
        ) {
            Text(
                text = "Try on",
                style = MaterialTheme.typography.body2,
            )
        }

        Spacer(Modifier.height(innerVerticalPadding / 2))
    }
}
