package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.components.footer.inactive

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.core.size.rememberScreenSize
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonSizes
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.sku.ProductGenerationUIStatus
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.icons.AiutaLoadingIcon
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.components.AiutaLabel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.utils.solveLoadingGenerationText
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.transitionAnimation

@Composable
internal fun InactiveFooter(modifier: Modifier = Modifier) {
    val screenSize = rememberScreenSize()
    val horizontalPadding = screenSize.widthDp * 0.2f

    Column(
        modifier = modifier.padding(horizontal = horizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(40.dp))

        ImageSelectorLoaderLabel()

        Spacer(Modifier.weight(1f))

        AiutaLabel()

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun ImageSelectorLoaderLabel(
    modifier: Modifier = Modifier,
) {
    val controller = LocalController.current
    val theme = LocalTheme.current

    val generationStatus = controller.generationStatus
    val skuGenerationTransition = updateTransition(
        targetState = generationStatus.value,
        label = "skuGenerationTransition",
    )

    val solvedText = solveLoadingGenerationText()
    val textTransition = updateTransition(solvedText.value)

    skuGenerationTransition.AnimatedVisibility(
        modifier = modifier,
        visible = { it == ProductGenerationUIStatus.LOADING },
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AiutaLoadingIcon(
                modifier = Modifier.size(16.dp),
                circleColor = theme.color.primary,
            )

            Spacer(Modifier.width(8.dp))

            textTransition.AnimatedContent(
                transitionSpec = { transitionAnimation },
            ) { text ->
                Text(
                    text = text,
                    style = FashionButtonSizes.mSize().textStyle,
                    color = theme.color.primary,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
