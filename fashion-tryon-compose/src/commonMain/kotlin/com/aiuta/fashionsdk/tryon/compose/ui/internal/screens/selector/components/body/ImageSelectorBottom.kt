package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.components.body

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalRippleConfiguration
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.configuration.features.picker.AiutaImagePickerFeature
import com.aiuta.fashionsdk.configuration.features.picker.history.AiutaImagePickerUploadsHistoryFeature
import com.aiuta.fashionsdk.configuration.features.styles.AiutaComponentStyle
import com.aiuta.fashionsdk.configuration.features.tryon.loading.AiutaTryOnLoadingPageFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaBottomSheetNavigator
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.sku.ProductGenerationUIStatus
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.icons.AiutaLoadingIcon
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnBottomSheetScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.models.ImageSelectorState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.utils.solveLoadingGenerationText
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.provideFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.strictProvideFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.transitionAnimation
import com.aiuta.fashionsdk.tryon.compose.uikit.button.FashionButton
import com.aiuta.fashionsdk.tryon.compose.uikit.button.FashionButtonSizes
import com.aiuta.fashionsdk.tryon.compose.uikit.button.FashionButtonStyles
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

@Composable
internal fun ImageSelectorBottom(
    modifier: Modifier = Modifier,
    hazeState: HazeState,
    uploadPhoto: () -> Unit,
) {
    val bottomSheetNavigator = LocalAiutaBottomSheetNavigator.current
    val controller = LocalController.current
    val theme = LocalTheme.current

    val imageSelectorFeature = strictProvideFeature<AiutaImagePickerFeature>()
    val loadingPageFeature = strictProvideFeature<AiutaTryOnLoadingPageFeature>()
    val uploadsHistoryFeature = provideFeature<AiutaImagePickerUploadsHistoryFeature>()

    val generationStatus = controller.generationStatus
    val countGeneratedOperation =
        controller.generatedOperationInteractor
            .countGeneratedOperation()
            .collectAsState(0)

    val sharedModifier = Modifier.wrapContentWidth()
    val sharedButtonSize = FashionButtonSizes.mSize()

    val sharedColor = theme.color.background.copy(alpha = 0.4f)
    val sharedBlurModifer =
        Modifier
            .clip(sharedButtonSize.shape)
            .hazeEffect(hazeState) {
                blurRadius = 10.dp
                backgroundColor = sharedColor
                tints = listOf(HazeTint(sharedColor))
                fallbackTint = HazeTint(sharedColor)
            }

    val bottomState =
        updateTransition(
            targetState =
            when {
                generationStatus.value == ProductGenerationUIStatus.LOADING -> {
                    ImageSelectorState.GENERATION_LOADING
                }

                else -> ImageSelectorState.LAST_IMAGE_SAVED
            },
            label = "bottomState",
        )

    bottomState.AnimatedContent(
        modifier = modifier,
        transitionSpec = { transitionAnimation },
    ) { state ->
        when (state) {
            ImageSelectorState.LAST_IMAGE_SAVED -> {
                uploadsHistoryFeature?.let {
                    val buttonStyle =
                        imageSelectorFeature.uploadsHistory?.styles?.changePhotoButtonStyle
                    styleChangePhotoButton(buttonStyle) {
                        FashionButton(
                            modifier = when (buttonStyle) {
                                AiutaComponentStyle.BLURRED -> sharedModifier.then(sharedBlurModifer)
                                else -> sharedModifier
                            },
                            text = uploadsHistoryFeature.strings.uploadsHistoryButtonChangePhoto,
                            style = when (buttonStyle) {
                                AiutaComponentStyle.BLURRED -> FashionButtonStyles.primaryStyle(
                                    backgroundColor = Color.Transparent,
                                    contentColor = theme.color.primary,
                                )

                                else -> FashionButtonStyles.primaryStyle(theme)
                            },
                            size = sharedButtonSize,
                            onClick = {
                                if (countGeneratedOperation.value == 0) {
                                    uploadPhoto()
                                } else {
                                    bottomSheetNavigator.show(
                                        TryOnBottomSheetScreen.GeneratedOperations,
                                    )
                                }
                            },
                        )
                    }
                }
            }

            ImageSelectorState.GENERATION_LOADING -> {
                val finalModifier = when (loadingPageFeature.styles.loadingStatusStyle) {
                    AiutaComponentStyle.BRAND -> sharedModifier.background(
                        color = theme.color.brand,
                        shape = sharedButtonSize.shape,
                    )

                    AiutaComponentStyle.BLURRED -> sharedModifier.then(sharedBlurModifer)
                    AiutaComponentStyle.BLURRED_WITH_OUTLINE ->
                        sharedModifier
                            .border(
                                width = 1.dp,
                                color = theme.color.border,
                                shape = sharedButtonSize.shape,
                            )
                            .then(sharedBlurModifer)

                    AiutaComponentStyle.CONTRAST -> sharedModifier.background(
                        color = theme.color.onLight,
                        shape = sharedButtonSize.shape,
                    )

                    AiutaComponentStyle.CONTRAST_INVERTED -> sharedModifier.background(
                        color = theme.color.onDark,
                        shape = sharedButtonSize.shape,
                    )
                }
                val contentColor = when (loadingPageFeature.styles.loadingStatusStyle) {
                    AiutaComponentStyle.BRAND -> theme.color.onDark
                    AiutaComponentStyle.CONTRAST_INVERTED -> theme.color.onLight
                    else -> theme.color.primary
                }

                val solvedText = solveLoadingGenerationText()
                val textTransition = updateTransition(solvedText.value)

                Row(
                    modifier = finalModifier.padding(
                        horizontal = 24.dp,
                        vertical = 12.dp,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    AiutaLoadingIcon(
                        modifier = Modifier.size(14.dp),
                        circleColor = contentColor,
                    )

                    Spacer(Modifier.width(8.dp))

                    textTransition.AnimatedContent(
                        transitionSpec = { transitionAnimation },
                    ) { text ->
                        Text(
                            text = text,
                            style = sharedButtonSize.textStyle,
                            color = contentColor,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun styleChangePhotoButton(
    buttonStyle: AiutaComponentStyle?,
    content: @Composable () -> Unit,
) {
    when (buttonStyle) {
        AiutaComponentStyle.BLURRED -> {
            CompositionLocalProvider(
                LocalRippleConfiguration provides null,
                content = content,
            )
        }

        else -> {
            content()
        }
    }
}
