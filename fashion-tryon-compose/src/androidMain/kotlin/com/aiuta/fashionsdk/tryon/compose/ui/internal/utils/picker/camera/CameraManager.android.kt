package com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.picker.camera

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.aiuta.fashionsdk.io.AiutaPlatformFile
import com.aiuta.fashionsdk.logger.d
import com.aiuta.fashionsdk.logger.e
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaLogger
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.picker.newImageUri

@Composable
internal actual fun rememberCameraManager(onResult: (AiutaPlatformFile) -> Unit): CameraManager {
    val context = LocalContext.current
    val logger = LocalAiutaLogger.current

    var tempPhotoUri by remember { mutableStateOf(value = Uri.EMPTY) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                logger?.d("CameraManager: successfully get image with uri - $tempPhotoUri")
                tempPhotoUri?.let { onResult.invoke(AiutaPlatformFile(tempPhotoUri)) }
            } else {
                logger?.e("CameraManager: failed to get image from camera")
            }
        },
    )

    return remember {
        CameraManager(
            onLaunch = {
                tempPhotoUri = newImageUri(
                    context = context,
                    logger = logger,
                )
                tempPhotoUri?.let { uri ->
                    cameraLauncher.launch(uri)
                }
            },
        )
    }
}

internal actual class CameraManager actual constructor(
    private val onLaunch: () -> Unit,
) {
    actual fun launch() {
        onLaunch()
    }
}
