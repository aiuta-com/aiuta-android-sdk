package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.content.nonempty

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaBottomSheetNavigator
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.sku.ProductGenerationUIStatus
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.isLastSavedPhotoAvailable
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnBottomSheetScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.components.body.ImageSelectorBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.components.footer.ImageSelectorFooter
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.MAIN_IMAGE_SIZE

@Composable
internal fun ImageSelectorScreenNonEmptyContent(modifier: Modifier = Modifier) {
    val bottomSheetNavigator = LocalAiutaBottomSheetNavigator.current
    val controller = LocalController.current
    val generationStatus = controller.generationStatus

    val isLastSavedPhotoAvailable = controller.isLastSavedPhotoAvailable()
    val isTryOnButtonVisible =
        remember {
            derivedStateOf {
                generationStatus.value != ProductGenerationUIStatus.LOADING && isLastSavedPhotoAvailable.value
            }
        }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ImageSelectorBlock(
            modifier = Modifier.fillMaxSize(MAIN_IMAGE_SIZE),
            uploadPhoto = {
                bottomSheetNavigator.show(
                    newSheetScreen =
                    TryOnBottomSheetScreen.ImagePicker(
                        originPageId = AiutaAnalyticsPageId.IMAGE_PICKER,
                    ),
                )
            },
        )

        ImageSelectorFooter(
            modifier =
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            isTryOnButtonVisible = isTryOnButtonVisible,
        )
    }
}
