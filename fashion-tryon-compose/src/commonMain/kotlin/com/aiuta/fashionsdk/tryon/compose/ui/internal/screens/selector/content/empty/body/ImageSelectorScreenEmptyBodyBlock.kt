package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.content.empty.body

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.compose.core.size.rememberScreenSize
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaDrawableResource
import com.aiuta.fashionsdk.compose.uikit.button.FashionButton
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonSizes
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonStyles
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.compose.uikit.utils.provideFeature
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentStandaloneImagePickerPageFeature
import com.aiuta.fashionsdk.configuration.features.picker.AiutaImagePickerFeature
import com.aiuta.fashionsdk.configuration.features.picker.model.AiutaImagePickerPredefinedModelFeature
import com.aiuta.fashionsdk.configuration.features.picker.protection.AiutaImagePickerProtectionDisclaimerFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaBottomSheetNavigator
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnBottomSheetScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.shadow.dropShadow

@Composable
internal fun ImageSelectorScreenEmptyBodyBlock(modifier: Modifier) {
    val bottomSheetNavigator = LocalAiutaBottomSheetNavigator.current
    val controller = LocalController.current
    val navigationController = LocalAiutaNavigationController.current
    val theme = LocalTheme.current

    val screenSize = rememberScreenSize()
    val imageBlockPadding = if (screenSize.heightDp > 900.dp) {
        screenSize.widthDp * 0.15f
    } else {
        screenSize.widthDp * 0.05f
    }

    val imageSelectorFeature = strictProvideFeature<AiutaImagePickerFeature>()
    val predefinedModelFeature = provideFeature<AiutaImagePickerPredefinedModelFeature>()
    val protectionFeature = provideFeature<AiutaImagePickerProtectionDisclaimerFeature>()
    val standaloneImagePickerPageFeature =
        provideFeature<AiutaConsentStandaloneImagePickerPageFeature>()

    val shouldShowConsent = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        shouldShowConsent.value = standaloneImagePickerPageFeature?.let {
            controller.consentInteractor.shouldShowConsent(standaloneImagePickerPageFeature)
        } ?: false
    }

    Column(
        modifier = modifier
            .padding(horizontal = 26.dp)
            .background(
                color = theme.color.neutral,
                shape = RoundedCornerShape(24.dp),
            )
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(60.dp))

        ImagesBlock(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(imageBlockPadding),
        )

        Spacer(Modifier.height(60.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = imageSelectorFeature.strings.imagePickerTitleEmpty,
            style = theme.label.typography.titleM,
            color = theme.color.primary,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = imageSelectorFeature.strings.imagePickerDescriptionEmpty,
            style = theme.label.typography.subtle,
            color = theme.color.primary,
            textAlign = TextAlign.Center,
        )

        protectionFeature?.let {
            Spacer(Modifier.height(16.dp))

            ProtectionBlock(
                modifier = Modifier.fillMaxWidth(),
                protectionFeature = protectionFeature,
            )
        }

        Spacer(Modifier.height(32.dp))

        FashionButton(
            modifier = Modifier.fillMaxWidth(),
            text = imageSelectorFeature.strings.imagePickerButtonUploadImage,
            style = FashionButtonStyles.primaryStyle(theme),
            size = FashionButtonSizes.lSize(),
            onClick = {
                val showPickerSheet = {
                    bottomSheetNavigator.show(
                        newSheetScreen = TryOnBottomSheetScreen.ImagePicker(
                            originPageId = AiutaAnalyticsPageId.IMAGE_PICKER,
                        ),
                    )
                }
                if (shouldShowConsent.value) {
                    navigationController.navigateTo(TryOnScreen.Consent(onObtainedConsents = showPickerSheet))
                } else {
                    showPickerSheet()
                }
            },
        )

        predefinedModelFeature?.let {
            Spacer(Modifier.height(20.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = predefinedModelFeature.strings.predefinedModelOr,
                style = theme.label.typography.subtle,
                color = theme.color.primary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(20.dp))

            FashionButton(
                modifier = Modifier.fillMaxWidth(),
                text = predefinedModelFeature.strings.predefinedModelPageTitle,
                style = FashionButtonStyles.adaptiveContrastStyle(theme),
                size = FashionButtonSizes.lSize(),
                onClick = {
                    navigationController.navigateTo(
                        newScreen = TryOnScreen.ModelSelector,
                    )
                },
            )

            Spacer(Modifier.height(32.dp))
        } ?: Spacer(Modifier.weight(0.5f))
    }
}

@Composable
private fun ImagesBlock(modifier: Modifier = Modifier) {
    val density = LocalDensity.current

    val imageSelectorFeature = strictProvideFeature<AiutaImagePickerFeature>()
    val paddingPx = with(density) { (32.dp).toPx() }

    Box(
        modifier = modifier,
    ) {
        ImageContainer(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    translationX = paddingPx
                    rotationZ = 10f
                },
            image = imageSelectorFeature.images.examples.getOrNull(1),
        )

        ImageContainer(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    translationX = -paddingPx
                    rotationZ = -12f
                },
            image = imageSelectorFeature.images.examples.getOrNull(0),
        )
    }
}

@Composable
private fun ImageContainer(
    modifier: Modifier = Modifier,
    image: AiutaDrawableResource?,
) {
    if (image == null) return

    val theme = LocalTheme.current

    val sharedShape = RoundedCornerShape(16.dp)

    AiutaImage(
        modifier = modifier
            .fillMaxHeight()
            .aspectRatio(0.63f)
            .dropShadow(
                shape = sharedShape,
                color = theme.color.primary.copy(alpha = 0.06f),
                blur = (13.42).dp,
                offsetY = (13.42).dp,
            )
            .clip(sharedShape)
            .border(
                width = 4.dp,
                color = theme.color.onDark,
                shape = sharedShape,
            ),
        image = image,
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}
