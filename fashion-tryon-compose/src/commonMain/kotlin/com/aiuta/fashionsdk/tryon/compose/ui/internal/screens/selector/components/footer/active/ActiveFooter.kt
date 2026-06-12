package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.components.footer.active

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.LocalPlatformContext
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.compose.uikit.button.FashionButton
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonSizes
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonStyles
import com.aiuta.fashionsdk.compose.uikit.composition.LocalAiutaFeatures
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.utils.provideFeature
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.picker.history.AiutaImagePickerUploadsHistoryFeature
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
internal fun ActiveFooter(modifier: Modifier = Modifier) {
    val coilContext = LocalPlatformContext.current
    val features = LocalAiutaFeatures.current
    val dialogController = LocalAiutaDialogController.current
    val errorSnackbarController = LocalAiutaErrorSnackbarController.current
    val navigationController = LocalAiutaNavigationController.current
    val bottomSheetNavigator = LocalAiutaBottomSheetNavigator.current
    val controller = LocalController.current
    val theme = LocalTheme.current

    val tryOnFeature = strictProvideFeature<AiutaTryOnFeature>()
    val inputImageValidationFeature = strictProvideFeature<AiutaTryOnInputImageValidationFeature>()
    val uploadsHistoryFeature = provideFeature<AiutaImagePickerUploadsHistoryFeature>()

    val countGeneratedOperation = controller.generatedOperationInteractor
        .countGeneratedOperation()
        .collectAsStateWithLifecycle(0)

    Column(
        modifier = modifier.padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(30.dp))

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

        uploadsHistoryFeature?.let {
            Spacer(Modifier.height(8.dp))

            FashionButton(
                modifier = Modifier.fillMaxWidth(),
                text = uploadsHistoryFeature.strings.uploadsHistoryButtonChangePhoto,
                style = FashionButtonStyles.primaryStyle(
                    backgroundColor = theme.color.neutral,
                    contentColor = theme.color.primary,
                ),
                size = FashionButtonSizes.lSize(),
                onClick = {
                    bottomSheetNavigator.show(
                        newSheetScreen = if (countGeneratedOperation.value == 0) {
                            TryOnBottomSheetScreen.ImagePicker(
                                originPageId = AiutaAnalyticsPageId.IMAGE_PICKER,
                            )
                        } else {
                            TryOnBottomSheetScreen.GeneratedOperations
                        },
                    )
                },
            )
        }

        Spacer(Modifier.weight(1f))
    }
}
