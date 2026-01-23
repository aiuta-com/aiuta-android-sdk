package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.components.footer.active

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.compose.uikit.button.FashionButton
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonSizes
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonStyles
import com.aiuta.fashionsdk.compose.uikit.composition.LocalAiutaFeatures
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.tryon.AiutaTryOnFeature
import com.aiuta.fashionsdk.configuration.features.tryon.validation.AiutaTryOnInputImageValidationFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaBottomSheetNavigator
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaDialogController
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaErrorSnackbarController
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnBottomSheetScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.utils.startGeneration

@Composable
internal fun ColumnScope.SingleTryOnFooterContent(
    modifier: Modifier = Modifier,
) {
    val coilContext = LocalPlatformContext.current
    val controller = LocalController.current
    val features = LocalAiutaFeatures.current
    val dialogController = LocalAiutaDialogController.current
    val errorSnackbarController = LocalAiutaErrorSnackbarController.current
    val navigationController = LocalAiutaNavigationController.current
    val theme = LocalTheme.current

    val tryOnFeature = strictProvideFeature<AiutaTryOnFeature>()
    val inputImageValidationFeature = strictProvideFeature<AiutaTryOnInputImageValidationFeature>()

    Spacer(Modifier.height(16.dp))

    ProductBlock(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth(),
    )

    Spacer(Modifier.height(24.dp))

    FashionButton(
        modifier = Modifier.fillMaxWidth(),
        text = tryOnFeature.strings.tryOn,
        style = tryOnFeature.styles.tryOnButtonGradient?.let { tryOnButtonGradient ->
            FashionButtonStyles.gradientColors(
                contentColor = theme.color.onDark,
                gradientBackground = Brush.horizontalGradient(tryOnButtonGradient),
            )
        } ?: FashionButtonStyles.primaryStyle(theme),
        size = FashionButtonSizes.lSize(),
        icon = tryOnFeature.icons.tryOn20,
        onClick = {
            controller.startGeneration(
                coilContext = coilContext,
                dialogController = dialogController,
                errorSnackbarController = errorSnackbarController,
                navigationController = navigationController,
                features = features,
                inputImageValidationStrings = inputImageValidationFeature.strings,
            )
        },
    )

    Spacer(Modifier.height(16.dp))
}

@Composable
private fun ProductBlock(modifier: Modifier = Modifier) {
    val bottomSheetNavigator = LocalAiutaBottomSheetNavigator.current
    val controller = LocalController.current
    val theme = LocalTheme.current

    val activeSKUItem = controller.activeProductItems.first()
    val sharedCorner = RoundedCornerShape(size = 8.dp)

    Row(
        modifier = modifier.clickableUnindicated {
            bottomSheetNavigator.show(
                TryOnBottomSheetScreen.ProductInfo(
                    primaryButtonState = TryOnBottomSheetScreen.ProductInfo.PrimaryButtonState.ADD_TO_CART,
                    originPageId = AiutaAnalyticsPageId.IMAGE_PICKER,
                    productItem = activeSKUItem,
                ),
            )
        },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AiutaImage(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(0.7f)
                .border(
                    width = 1.dp,
                    color = theme.color.border,
                    shape = sharedCorner,
                )
                .clip(sharedCorner),
            imageUrl = activeSKUItem.imageUrls.firstOrNull(),
            shape = sharedCorner,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = activeSKUItem.brand,
                style = theme.productBar.typography.brand,
                color = theme.color.primary,
                textAlign = TextAlign.Start,
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = activeSKUItem.title,
                style = theme.productBar.typography.product,
                color = theme.color.primary,
                textAlign = TextAlign.Start,
            )
        }

        Spacer(Modifier.width(8.dp))

        AiutaIcon(
            modifier =
            Modifier
                .size(16.dp)
                .align(Alignment.CenterVertically),
            icon = theme.productBar.icons.arrow16,
            contentDescription = null,
            tint = theme.color.border,
        )
    }
}
