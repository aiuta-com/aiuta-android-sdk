package com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.picker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPickerEventType
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaIcon
import com.aiuta.fashionsdk.configuration.features.picker.camera.AiutaImagePickerCameraFeature
import com.aiuta.fashionsdk.configuration.features.picker.gallery.AiutaImagePickerPhotoGalleryFeature
import com.aiuta.fashionsdk.configuration.features.picker.model.AiutaImagePickerPredefinedModelFeature
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.LastSavedImages
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendPickerAnalytic
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.activateAutoTryOn
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaTryOnDialogController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.dialog.AiutaTryOnDialogState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.dialog.hideDialog
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.dialog.showDialog
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.navigateTo
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.NavigationBottomSheetScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.NavigationScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.components.SheetDivider
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.picker.exceptions.NotSupportedImageSourceException
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.provideFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.permission.AiutaPickerSource
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.permission.actionWithPermission
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.permission.isPickerSourceAvailable
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.permission.rememberAiutaPermissionHandler
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.picker.camera.rememberCameraManager
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.picker.file.rememberImagePickerLauncher
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.tryon.compose.uikit.utils.clickableUnindicated

@Composable
internal fun ColumnScope.ImagePickerSheet(pickerData: NavigationBottomSheetScreen.ImagePicker) {
    val controller = LocalController.current
    val dialogController = LocalAiutaTryOnDialogController.current

    val cameraFeature = provideFeature<AiutaImagePickerCameraFeature>()
    val photoGalleryFeature = provideFeature<AiutaImagePickerPhotoGalleryFeature>()
    val predefinedModelFeature = provideFeature<AiutaImagePickerPredefinedModelFeature>()

    val pickerFeatures = remember {
        listOfNotNull(
            cameraFeature.takeIf { isPickerSourceAvailable(AiutaPickerSource.CAMERA) },
            photoGalleryFeature,
            predefinedModelFeature,
        )
    }

    val sharedModifier = Modifier
        .fillMaxWidth()
        .height(74.dp)

    val permissionHandler = rememberAiutaPermissionHandler()

    val scope = rememberCoroutineScope()

    val cameraManager =
        rememberCameraManager { file ->
            controller.sendPickerAnalytic(
                event = AiutaAnalyticsPickerEventType.NEW_PHOTO_TAKEN,
                pageId = pickerData.originPageId,
            )
            controller.lastSavedImages.value =
                LastSavedImages.PlatformImageSource(
                    platformFiles = listOf(file),
                )
            // Activate try on
            controller.activateAutoTryOn()
            // Move back
            controller.bottomSheetNavigator.hide()
        }

    val galleryManager = rememberImagePickerLauncher { files ->
        controller.sendPickerAnalytic(
            event = AiutaAnalyticsPickerEventType.GALLERY_PHOTO_SELECTED,
            pageId = pickerData.originPageId,
        )
        controller.lastSavedImages.value = LastSavedImages.PlatformImageSource(files)
        // Activate try on
        controller.activateAutoTryOn()
        // Move back
        controller.bottomSheetNavigator.hide()
    }

    SheetDivider()

    Spacer(Modifier.height(4.dp))

    pickerFeatures.forEachIndexed { index, feature ->
        key("feature_$index") {
            PickerButton(
                modifier = sharedModifier,
                shouldDrawDivider = index != pickerFeatures.lastIndex,
                icon = when (feature) {
                    is AiutaImagePickerCameraFeature -> feature.icons.camera24
                    is AiutaImagePickerPhotoGalleryFeature -> feature.icons.gallery24
                    is AiutaImagePickerPredefinedModelFeature -> feature.icons.selectModels24
                    else -> throw NotSupportedImageSourceException()
                },
                text = when (feature) {
                    is AiutaImagePickerCameraFeature -> feature.strings.cameraButtonTakePhoto
                    is AiutaImagePickerPhotoGalleryFeature -> feature.strings.galleryButtonSelectPhoto
                    is AiutaImagePickerPredefinedModelFeature -> feature.strings.predefinedModelPageTitle
                    else -> throw NotSupportedImageSourceException()
                },
                onClick = {
                    when (feature) {
                        is AiutaImagePickerCameraFeature -> {
                            scope.actionWithPermission(
                                pickerSource = AiutaPickerSource.CAMERA,
                                permissionHandler = permissionHandler,
                                onGranted = {
                                    cameraManager.launch()
                                },
                                onAlwaysDenied = {
                                    controller.bottomSheetNavigator.hide()
                                    dialogController.showDialog(
                                        dialogState =
                                        AiutaTryOnDialogState(
                                            title = feature.strings.cameraPermissionTitle,
                                            description = feature.strings.cameraPermissionDescription,
                                            confirmButton = feature.strings.cameraPermissionButtonOpenSettings,
                                            onConfirm = permissionHandler::openAppSettings,
                                            onDismiss = dialogController::hideDialog,
                                        ),
                                    )
                                },
                            )
                        }

                        is AiutaImagePickerPhotoGalleryFeature -> {
                            controller.sendPickerAnalytic(
                                event = AiutaAnalyticsPickerEventType.PHOTO_GALLERY_OPENED,
                                pageId = pickerData.originPageId,
                            )
                            scope.actionWithPermission(
                                pickerSource = AiutaPickerSource.GALLERY,
                                permissionHandler = permissionHandler,
                                onGranted = {
                                    galleryManager.launch()
                                },
                                onAlwaysDenied = {
                                    // Show nothing
                                    controller.bottomSheetNavigator.hide()
                                },
                            )
                        }

                        is AiutaImagePickerPredefinedModelFeature -> {
                            controller.bottomSheetNavigator.hide()
                            controller.navigateTo(NavigationScreen.ModelSelector)
                        }

                        else -> throw NotSupportedImageSourceException()
                    }
                },
            )
        }
    }
}

@Composable
private fun PickerButton(
    modifier: Modifier = Modifier,
    icon: AiutaIcon,
    text: String,
    shouldDrawDivider: Boolean = true,
    onClick: () -> Unit,
) {
    val theme = LocalTheme.current

    Column {
        Row(
            modifier = modifier
                .clickableUnindicated { onClick() }
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AiutaIcon(
                modifier = Modifier.size(24.dp),
                icon = icon,
                contentDescription = null,
                tint = theme.color.brand,
            )

            Spacer(Modifier.width(16.dp))

            Box(
                modifier = Modifier.weight(1f).fillMaxHeight(),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = text,
                    style = theme.label.typography.regular,
                    color = theme.color.primary,
                )
            }
        }

        if (shouldDrawDivider) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 56.dp.takeIf {
                            theme.bottomSheet.toggles.extendDelimitersToTheLeft
                        } ?: 0.dp,
                        end = 16.dp.takeIf {
                            theme.bottomSheet.toggles.extendDelimitersToTheRight
                        } ?: 0.dp,
                    ),
                color = theme.color.neutral,
            )
        }
    }
}
