package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPickerEventType
import com.aiuta.fashionsdk.compose.core.size.rememberScreenSize
import com.aiuta.fashionsdk.compose.uikit.button.FashionButton
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonSizes
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonStyles
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.configuration.features.tryon.AiutaTryOnFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.config.features.toUrlImage
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.LastSavedImages
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.screen.model.ModelSelectorScreenState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendPickerAnalytic
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.activateAutoTryOn
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.utils.MODEL_IMAGE_BOTTOM_PADDING_COEF
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.utils.MODEL_IMAGE_HORIZONTAL_PADDING_COEF
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.strictProvideFeature

@Composable
internal fun ModelSelectorShowContent(
    modifier: Modifier = Modifier,
    state: ModelSelectorScreenState.Content,
) {
    val controller = LocalController.current
    val navigationController = LocalAiutaNavigationController.current
    val theme = LocalTheme.current

    val screenSize = rememberScreenSize()
    val tryOnFeature = strictProvideFeature<AiutaTryOnFeature>()

    val imageHorizontalPadding = screenSize.widthDp * MODEL_IMAGE_HORIZONTAL_PADDING_COEF
    val bottomPadding = screenSize.heightDp * MODEL_IMAGE_BOTTOM_PADDING_COEF

    val activeCategory = remember {
        mutableStateOf(
            with(state) {
                val defaultCategory = state.categories.firstOrNull()

                preferredCategoryId?.let {
                    categories.find { model ->
                        model.id == preferredCategoryId
                    } ?: defaultCategory
                } ?: defaultCategory
            },
        )
    }
    val activeImageModel = remember { mutableStateOf(activeCategory.value?.models?.firstOrNull()) }

    val sharedShape = RoundedCornerShape(24.dp)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AiutaImage(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = imageHorizontalPadding)
                .clip(sharedShape),
            imageUrl = activeImageModel.value?.url,
            shape = sharedShape,
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )

        Spacer(Modifier.height(26.dp))

        ModelsCategoriesBlock(
            state = state,
            activeCategory = activeCategory,
        )

        Spacer(Modifier.height(30.dp))

        ModelsListBlock(
            modifier = Modifier.fillMaxWidth(),
            activeCategory = activeCategory,
            activeImageModel = activeImageModel,
        )

        Spacer(Modifier.height(20.dp))

        Column(
            modifier = Modifier.height(bottomPadding),
        ) {
            Spacer(Modifier.weight(1f))

            FashionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
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
                    activeImageModel.value?.let { model ->
                        controller.sendPickerAnalytic(
                            event = AiutaAnalyticsPickerEventType.PREDEFINED_MODEL_SELECTED,
                            pageId = AiutaAnalyticsPageId.IMAGE_PICKER,
                        )

                        // Save model
                        controller.lastSavedImages.value =
                            LastSavedImages.UrlSource.PregeneratedModels(
                                urlImages = listOf(model.toUrlImage()),
                            )
                        // Activate try on
                        controller.activateAutoTryOn()

                        // Go back to picker
                        navigationController.navigateTo(TryOnScreen.ImageSelector)
                    }
                },
            )

            Spacer(Modifier.weight(1f))
        }
    }
}
