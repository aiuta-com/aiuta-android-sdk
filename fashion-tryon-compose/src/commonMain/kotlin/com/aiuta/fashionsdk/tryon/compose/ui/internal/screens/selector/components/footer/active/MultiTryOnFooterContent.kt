package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.components.footer.active

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem
import com.aiuta.fashionsdk.configuration.features.tryon.AiutaTryOnFeature
import com.aiuta.fashionsdk.configuration.features.tryon.validation.AiutaTryOnInputImageValidationFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaBottomSheetNavigator
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaDialogController
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaErrorSnackbarController
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaFeatures
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnBottomSheetScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.utils.startGeneration
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.strictProvideFeature
import com.aiuta.fashionsdk.tryon.compose.uikit.button.FashionButton
import com.aiuta.fashionsdk.tryon.compose.uikit.button.FashionButtonSizes
import com.aiuta.fashionsdk.tryon.compose.uikit.button.FashionButtonStyles
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.tryon.compose.uikit.utils.clickableUnindicated

@Composable
internal fun ColumnScope.MultiTryOnFooterContent(
    modifier: Modifier = Modifier,
) {
    val coilContext = LocalPlatformContext.current
    val controller = LocalController.current
    val features = LocalAiutaFeatures.current
    val dialogController = LocalAiutaDialogController.current
    val navigationController = LocalAiutaNavigationController.current
    val errorSnackbarController = LocalAiutaErrorSnackbarController.current
    val theme = LocalTheme.current

    val tryOnFeature = strictProvideFeature<AiutaTryOnFeature>()
    val inputImageValidationFeature = strictProvideFeature<AiutaTryOnInputImageValidationFeature>()

    Spacer(Modifier.height(20.dp))

    Text(
        text = tryOnFeature.strings.outfitTitle,
        style = theme.productBar.typography.product,
        color = theme.color.primary,
    )

    Spacer(Modifier.height(12.dp))

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutfitItemsRow(
            modifier = Modifier.weight(1f).height(57.dp),
            productItems = controller.activeProductItems,
        )

        Spacer(Modifier.width(16.dp))

        FashionButton(
            modifier = Modifier.width(146.dp),
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
    }

    Spacer(Modifier.height(36.dp))
}

@Composable
private fun OutfitItemsRow(
    modifier: Modifier = Modifier,
    productItems: List<ProductItem>,
    itemWidth: Dp = 54.dp,
    itemSpacing: Dp = 6.dp,
) {
    val bottomSheetNavigator = LocalAiutaBottomSheetNavigator.current
    val theme = LocalTheme.current

    val commonImageModifier = Modifier
        .width(itemWidth)
        .fillMaxHeight()
        .clip(theme.image.shapes.imageSShape)
    val imageModifier = if (theme.productBar.toggles.applyProductFirstImageExtraPadding) {
        commonImageModifier.padding(10.dp)
    } else {
        commonImageModifier
    }

    BoxWithConstraints(modifier = modifier) {
        val maxItems = remember(maxWidth, productItems.size) {
            val available = maxWidth
            val cell = itemWidth + itemSpacing

            // N * itemSize + (N - 1) * spacing <= available
            // N <= (available + spacing) / (itemSize + spacing)
            val canFit = ((available + itemSpacing) / cell).toInt()
            canFit.coerceIn(0, productItems.size)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(itemSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            productItems.take(maxItems).forEach { product ->
                key(product.id) {
                    Box(
                        modifier = commonImageModifier
                            .background(theme.color.neutral)
                            .clickableUnindicated {
                                bottomSheetNavigator.show(
                                    TryOnBottomSheetScreen.ProductInfo(
                                        primaryButtonState = TryOnBottomSheetScreen.ProductInfo.PrimaryButtonState.ADD_TO_CART,
                                        originPageId = AiutaAnalyticsPageId.IMAGE_PICKER,
                                        productItem = product,
                                    ),
                                )
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        AiutaImage(
                            modifier = imageModifier,
                            imageUrl = product.imageUrls.first(),
                            shape = theme.image.shapes.imageMShape,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}
