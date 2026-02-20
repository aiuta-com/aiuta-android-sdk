package com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.picker.file

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.interop.LocalUIViewController
import com.aiuta.fashionsdk.io.AiutaPlatformFile
import kotlin.coroutines.resume
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSItemProvider
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUUID
import platform.Foundation.dataWithContentsOfURL
import platform.Foundation.writeToURL
import platform.Photos.PHPhotoLibrary
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerConfigurationAssetRepresentationModeCurrent
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UniformTypeIdentifiers.UTTypeImage
import platform.darwin.NSObject

@Composable
internal actual fun rememberImagePickerLauncher(onResult: (List<AiutaPlatformFile>) -> Unit): ImagePickerLauncher {
    val scope = rememberCoroutineScope()
    val currentUIViewController = LocalUIViewController.current

    val pickerDelegate = remember {
        object :
            NSObject(),
            PHPickerViewControllerDelegateProtocol {
            override fun picker(
                picker: PHPickerViewController,
                didFinishPicking: List<*>,
            ) {
                scope.launch {
                    val results = didFinishPicking
                        .mapNotNull {
                            val result = it as? PHPickerResult ?: return@mapNotNull null

                            async {
                                result.itemProvider.loadFileRepresentationForTypeIdentifierSuspend()
                            }
                        }
                        .awaitAll()
                        .filterNotNull()

                    withContext(Dispatchers.Main) {
                        onResult(results)
                    }
                }

                picker.dismissViewControllerAnimated(true, null)
            }
        }
    }

    return remember(currentUIViewController) {
        ImagePickerLauncher(
            onLaunch = {
                val imagePicker = createPHPickerViewController(
                    delegate = pickerDelegate,
                )

                currentUIViewController.presentViewController(
                    imagePicker,
                    true,
                    null,
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

private suspend fun NSItemProvider.loadFileRepresentationForTypeIdentifierSuspend(): AiutaPlatformFile? = suspendCancellableCoroutine { continuation ->
    val progress = loadFileRepresentationForTypeIdentifier(
        typeIdentifier = registeredTypeIdentifiers.firstOrNull() as? String
            ?: UTTypeImage.identifier,
    ) { url, error ->
        if (error != null) {
            continuation.resume(null)
            return@loadFileRepresentationForTypeIdentifier
        }

        continuation.resume(
            url?.createTempFile()?.let { tempUrl ->
                AiutaPlatformFile(
                    url = url,
                    tempUrl = tempUrl,
                )
            },
        )
    }

    continuation.invokeOnCancellation {
        progress.cancel()
    }
}

private fun NSURL.createTempFile(): NSURL? {
    val extension = absoluteString
        ?.substringAfterLast('/')
        ?.substringAfterLast('.', "") ?: return null
    val data = NSData.dataWithContentsOfURL(this) ?: return null
    return NSURL.fileURLWithPath("${NSTemporaryDirectory()}/${NSUUID().UUIDString}.$extension")
        .apply {
            data.writeToURL(this, true)
        }
}

private fun createPHPickerViewController(
    delegate: PHPickerViewControllerDelegateProtocol,
): PHPickerViewController {
    val configuration = PHPickerConfiguration(PHPhotoLibrary.sharedPhotoLibrary())

    val newFilter =
        PHPickerFilter.anyFilterMatchingSubfilters(listOf(PHPickerFilter.imagesFilter()))
    configuration.filter = newFilter
    configuration.preferredAssetRepresentationMode =
        PHPickerConfigurationAssetRepresentationModeCurrent
    configuration.selectionLimit = 1
    val picker = PHPickerViewController(configuration)
    picker.delegate = delegate
    return picker
}
