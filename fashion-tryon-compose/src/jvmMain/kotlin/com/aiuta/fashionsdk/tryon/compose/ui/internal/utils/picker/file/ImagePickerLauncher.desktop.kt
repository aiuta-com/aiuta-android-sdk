package com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.picker.file

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.aiuta.fashionsdk.io.AiutaPlatformFile
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher

@Composable
internal actual fun rememberImagePickerLauncher(
    onResult: (List<AiutaPlatformFile>) -> Unit,
): ImagePickerLauncher {
    val nativeFilePicker = rememberFilePickerLauncher(
        type = FilePickerFileType.Image,
        selectionMode = FilePickerSelectionMode.Single,
    ) { files ->
        val aiutaFiles = files.map { nativeFile -> AiutaPlatformFile(nativeFile.file) }
        onResult(aiutaFiles)
    }

    return remember {
        ImagePickerLauncher(
            onLaunch = nativeFilePicker::launch,
        )
    }
}

internal actual class ImagePickerLauncher actual constructor(
    actual val onLaunch: () -> Unit,
) {
    actual fun launch() {
        onLaunch()
    }
}
