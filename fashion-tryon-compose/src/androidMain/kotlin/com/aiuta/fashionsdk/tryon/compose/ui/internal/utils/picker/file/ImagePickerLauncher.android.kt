package com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.picker.file

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.aiuta.fashionsdk.io.AiutaPlatformFile

@Composable
internal actual fun rememberImagePickerLauncher(onResult: (List<AiutaPlatformFile>) -> Unit): ImagePickerLauncher {
    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            onResult(uri?.let { listOf(AiutaPlatformFile(it)) } ?: emptyList())
        },
    )

    return remember {
        ImagePickerLauncher(
            onLaunch = {
                mediaPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                )
            },
        )
    }
}

internal actual class ImagePickerLauncher actual constructor(
    actual val onLaunch: () -> Unit,
) {
    public actual fun launch() {
        onLaunch()
    }
}
