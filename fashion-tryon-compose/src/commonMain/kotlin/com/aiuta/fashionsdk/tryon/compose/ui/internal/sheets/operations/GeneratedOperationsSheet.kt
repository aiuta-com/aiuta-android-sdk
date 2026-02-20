package com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.operations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPickerEventType
import com.aiuta.fashionsdk.compose.uikit.button.FashionButton
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonSizes
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonStyles
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.models.images.AiutaInputImage
import com.aiuta.fashionsdk.configuration.features.picker.history.AiutaImagePickerUploadsHistoryFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaBottomSheetNavigator
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaErrorSnackbarController
import com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.generated.operations.cleanLoadingUploads
import com.aiuta.fashionsdk.tryon.compose.domain.internal.utils.asCustom
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.toPublicHistory
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.operations.GeneratedOperationUIModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendPickerAnalytic
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.icons.AiutaBoxedLoadingIcon
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.activateAutoTryOn
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaTryOnLoadingActionsController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.loading.listenErrorDeletingUploadedImages
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.updateActiveOperationOrSetEmpty
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnBottomSheetScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.components.SheetDivider
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.operations.controller.GeneratedOperationsSheetListener
import kotlinx.coroutines.launch

@Composable
internal fun GeneratedOperationsSheet(
    modifier: Modifier = Modifier,
) {
    val bottomSheetNavigator = LocalAiutaBottomSheetNavigator.current
    val controller = LocalController.current
    val theme = LocalTheme.current

    val scope = rememberCoroutineScope()

    val uploadsHistoryFeature = strictProvideFeature<AiutaImagePickerUploadsHistoryFeature>()

    val sharedHorizontalPadding = 16.dp
    val sharedOperationsModifier =
        Modifier
            .width(148.dp)
            .height(254.dp)
            .clip(theme.image.shapes.imageMShape)

    val generatedOperations = controller.generatedOperationInteractor
        .getGeneratedOperationFlow()
        .collectAsLazyPagingItems()

    sendPickerAnalytic(
        event = AiutaAnalyticsPickerEventType.UPLOADS_HISTORY_OPENED,
        pageId = AiutaAnalyticsPageId.IMAGE_PICKER,
    )

    GeneratedOperationsSheetListener()

    Column(modifier = modifier) {
        SheetDivider()

        Spacer(Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(horizontal = sharedHorizontalPadding),
            text = uploadsHistoryFeature.strings.uploadsHistoryTitle,
            style = theme.label.typography.titleM,
            color = theme.color.primary,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(Modifier.height(24.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = sharedHorizontalPadding),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                count = generatedOperations.itemCount,
                key = generatedOperations.itemKey { it.operationId },
                contentType = generatedOperations.itemContentType { it },
            ) {
                val generatedOperation = generatedOperations[it]

                generatedOperation?.let {
                    OperationItem(
                        modifier = sharedOperationsModifier.animateItem(),
                        generatedOperation = generatedOperation,
                        onClick = {
                            with(controller) {
                                scope.launch {
                                    // Analytic
                                    sendPickerAnalytic(
                                        event = AiutaAnalyticsPickerEventType.UPLOADED_PHOTO_SELECTED,
                                        pageId = AiutaAnalyticsPageId.IMAGE_PICKER,
                                    )

                                    // Host notification
                                    val image = generatedOperation.urlImages.firstOrNull()
                                    image?.let {
                                        runCatching {
                                            uploadsHistoryFeature
                                                .dataProvider
                                                .asCustom()
                                                ?.selectUploadedImage(
                                                    image = AiutaInputImage(
                                                        id = image.imageId,
                                                        url = image.imageUrl,
                                                        type = image.imageType.toPublicHistory(),
                                                    ),
                                                )
                                        }
                                    }

                                    // Change
                                    updateActiveOperationOrSetEmpty(generatedOperation)
                                    // Activate auto try on
                                    activateAutoTryOn()
                                    // Move back
                                    bottomSheetNavigator.hide()
                                }
                            }
                        },
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        FashionButton(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = sharedHorizontalPadding),
            text = uploadsHistoryFeature.strings.uploadsHistoryButtonNewPhoto,
            style = FashionButtonStyles.primaryStyle(theme),
            size = FashionButtonSizes.lSize(),
            onClick = {
                bottomSheetNavigator.change(
                    newSheetScreen =
                    TryOnBottomSheetScreen.ImagePicker(
                        originPageId = AiutaAnalyticsPageId.IMAGE_PICKER,
                    ),
                )
            },
        )
    }
}

@Composable
private fun OperationItem(
    modifier: Modifier = Modifier,
    generatedOperation: GeneratedOperationUIModel,
    onClick: () -> Unit,
) {
    val controller = LocalController.current
    val loadingActionsController = LocalAiutaTryOnLoadingActionsController.current
    val errorSnackbarController = LocalAiutaErrorSnackbarController.current
    val theme = LocalTheme.current
    val scope = rememberCoroutineScope()

    val isLoadingScrimVisible = remember {
        derivedStateOf {
            loadingActionsController.loadingUploadsHolder.contain(generatedOperation)
        }
    }

    val isTrashVisible = remember {
        derivedStateOf {
            !isLoadingScrimVisible.value
        }
    }

    Box(
        modifier = modifier.clickableUnindicated(!isLoadingScrimVisible.value) { onClick() },
    ) {
        AiutaImage(
            modifier = Modifier
                .clipToBounds()
                .fillMaxSize(),
            imageUrl = generatedOperation.sourceImageUrls.firstOrNull(),
            shape = theme.image.shapes.imageMShape,
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp),
            visible = isTrashVisible.value,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            AiutaIcon(
                modifier = Modifier
                    .size(24.dp)
                    .clickableUnindicated {
                        scope.launch {
                            with(controller) {
                                sendPickerAnalytic(
                                    event = AiutaAnalyticsPickerEventType.UPLOADED_PHOTO_DELETED,
                                    pageId = AiutaAnalyticsPageId.IMAGE_PICKER,
                                )

                                // Add operation to loading list
                                loadingActionsController.loadingUploadsHolder.put(
                                    generatedOperation,
                                )

                                // Delete operations
                                generatedOperationInteractor
                                    .deleteOperation(generatedOperation)
                                    .listenErrorDeletingUploadedImages(
                                        controller = controller,
                                        errorSnackbarController = errorSnackbarController,
                                        loadingActionsController = loadingActionsController,
                                    )

                                // If local - let's remove from loading
                                val isLocalMode =
                                    generatedOperationInteractor.cleanLoadingUploads {
                                        loadingActionsController.loadingUploadsHolder.remove(
                                            generatedOperation,
                                        )
                                    }

                                // If active images is deleted and it's local  - let's get new first
                                val isActiveOperationDeleted =
                                    lastSavedOperation.value?.operationId == generatedOperation.operationId
                                if (isActiveOperationDeleted && isLocalMode) {
                                    // Try to get new first
                                    val newFirstOperation =
                                        generatedOperationInteractor.getFirstGeneratedOperation()

                                    // Try to update with new or set as empty
                                    updateActiveOperationOrSetEmpty(newFirstOperation)
                                }
                            }
                        }
                    },
                icon = theme.selectionSnackbar.icons.trash24,
                contentDescription = null,
                tint = theme.color.background,
            )
        }

        AnimatedVisibility(
            modifier = Modifier
                .clipToBounds()
                .fillMaxSize(),
            visible = isLoadingScrimVisible.value,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            AiutaBoxedLoadingIcon(
                modifier = Modifier.fillMaxSize(),
                circleColor = Color.White,
            )
        }
    }
}
