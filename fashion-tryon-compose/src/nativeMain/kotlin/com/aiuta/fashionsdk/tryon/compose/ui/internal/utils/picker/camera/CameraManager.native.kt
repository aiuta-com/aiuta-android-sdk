package com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.picker.camera

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.aiuta.fashionsdk.io.AiutaPlatformFile
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUUID
import platform.Foundation.writeToURL
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerCameraCaptureMode
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerEditedImage
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject

@Composable
internal actual fun rememberCameraManager(onResult: (AiutaPlatformFile) -> Unit): CameraManager {
    val imagePicker = UIImagePickerController()
    val cameraDelegate =
        remember {
            object :
                NSObject(),
                UIImagePickerControllerDelegateProtocol,
                UINavigationControllerDelegateProtocol {
                override fun imagePickerController(
                    picker: UIImagePickerController,
                    didFinishPickingMediaWithInfo: Map<Any?, *>,
                ) {
                    val image = didFinishPickingMediaWithInfo.getValue(
                        UIImagePickerControllerEditedImage,
                    ) as? UIImage ?: didFinishPickingMediaWithInfo.getValue(
                        UIImagePickerControllerOriginalImage,
                    ) as? UIImage

                    val imageUrl = image?.let { getImageNSURL(it) }

                    imageUrl?.let {
                        onResult.invoke(AiutaPlatformFile(imageUrl))
                    }
                    picker.dismissViewControllerAnimated(true, null)
                }
            }
        }

    return remember {
        CameraManager {
            imagePicker.setSourceType(
                UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera,
            )
            imagePicker.setAllowsEditing(true)
            imagePicker.setCameraCaptureMode(
                UIImagePickerControllerCameraCaptureMode.UIImagePickerControllerCameraCaptureModePhoto,
            )
            imagePicker.setDelegate(cameraDelegate)
            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                imagePicker,
                true,
                null,
            )
        }
    }
}

internal actual class CameraManager actual constructor(
    actual val onLaunch: () -> Unit,
) {
    actual fun launch() {
        onLaunch()
    }
}

private fun getImageNSURL(image: UIImage): NSURL? {
    // Convert UIImage to NSData (JPEG with quality = 1.0)
    val imageData = UIImageJPEGRepresentation(image, 1.0) ?: return null

    // Get temp directory path
    val tempDir = NSTemporaryDirectory()
    val fileName = "${NSUUID().UUIDString}.jpg"
    val fullPath = tempDir + fileName

    val fileURL = NSURL.fileURLWithPath(path = fullPath)

    // Write data to file
    return if (imageData.writeToURL(fileURL, atomically = true)) {
        fileURL
    } else {
        null
    }
}
