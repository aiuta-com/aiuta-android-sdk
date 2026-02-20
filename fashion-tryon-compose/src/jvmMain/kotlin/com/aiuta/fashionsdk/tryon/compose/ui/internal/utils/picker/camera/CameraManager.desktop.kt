package com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.picker.camera

import androidx.compose.runtime.Composable
import com.aiuta.fashionsdk.io.AiutaPlatformFile

@Composable
internal actual fun rememberCameraManager(onResult: (AiutaPlatformFile) -> Unit): CameraManager = CameraManager(
    onLaunch = {
        error("Camera is not supported on this platform")
    },
)

internal actual class CameraManager actual constructor(
    actual val onLaunch: () -> Unit,
) {
    actual fun launch() {
        error("Camera is not supported on this platform")
    }
}
