@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package com.aiuta.fashionsdk.io

import com.aiuta.fashionsdk.context.AiutaPlatformContext
import com.aiuta.fashionsdk.io.compression.AiutaCompressionConfig
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.Image
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.File
import org.w3c.files.FileReader

// External function declarations for WASM browser APIs
@JsFun("(canvas, callback, type, quality) => canvas.toBlob(callback, type, quality)")
private external fun canvasToBlob(canvas: HTMLCanvasElement, callback: (Blob?) -> Unit, type: String, quality: Double)

@JsFun("(img, callback) => { img.onerror = callback; }")
private external fun setImageOnError(img: Image, callback: () -> Unit)

public actual class AiutaPlatformFile(public val file: File)

public actual suspend fun AiutaPlatformFile.readByteArray(platformContext: AiutaPlatformContext): ByteArray = withContext(Dispatchers.Main) {
    suspendCoroutine { continuation ->
        val reader = FileReader()
        reader.onload = { event ->
            try {
                // Read the file as an ArrayBuffer
                val arrayBuffer = event
                    .target
                    ?.unsafeCast<FileReader>()
                    ?.result
                    ?.unsafeCast<ArrayBuffer>()
                    ?: error("Could not read file")

                // Convert the ArrayBuffer to a ByteArray
                val bytes = Uint8Array(arrayBuffer)

                // Copy the bytes into a ByteArray
                val byteArray = ByteArray(bytes.length)
                for (i in 0 until bytes.length) {
                    byteArray[i] = bytes[i]
                }

                // Return the ByteArray
                continuation.resume(byteArray)
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            }
        }

        // Read the file as an ArrayBuffer
        reader.readAsArrayBuffer(file)
    }
}

public actual suspend fun AiutaPlatformFile.readCompressedByteArray(
    platformContext: AiutaPlatformContext,
    config: AiutaCompressionConfig,
): ByteArray = try {
    resizeImageAndCompress(
        file = file,
        config = config,
    )
} catch (e: Exception) {
    readByteArray(platformContext = platformContext)
}

private suspend fun resizeImageAndCompress(
    file: File,
    config: AiutaCompressionConfig,
): ByteArray = withContext(Dispatchers.Main) {
    suspendCoroutine { continuation ->
        try {
            // Create an Image element to load the file
            val img = Image()
            val objectUrl = URL.createObjectURL(file)

            img.onload = {
                try {
                    // Revoke the object URL after loading
                    URL.revokeObjectURL(objectUrl)

                    // Calculate new dimensions
                    val inWidth = img.width
                    val inHeight = img.height

                    val (outWidth, outHeight) = if (inWidth > config.compressedImageMaxSize ||
                        inHeight > config.compressedImageMaxSize
                    ) {
                        calculateResizeDimensions(
                            inWidth = inWidth,
                            inHeight = inHeight,
                            maxSize = config.compressedImageMaxSize,
                        )
                    } else {
                        inWidth to inHeight
                    }

                    // Create canvas and draw resized image
                    val canvas = kotlinx.browser.document.createElement("canvas") as HTMLCanvasElement
                    canvas.width = outWidth
                    canvas.height = outHeight

                    val context = canvas.getContext("2d") as CanvasRenderingContext2D
                    context.drawImage(img, 0.0, 0.0, outWidth.toDouble(), outHeight.toDouble())

                    // Convert canvas to blob with compression
                    val quality = config.compressedImageQuality / 100.0
                    canvasToBlob(canvas, { blob ->
                        if (blob != null) {
                            // Convert blob to ByteArray
                            val reader = FileReader()
                            reader.onload = { event ->
                                try {
                                    val arrayBuffer = event
                                        .target
                                        ?.unsafeCast<FileReader>()
                                        ?.result
                                        ?.unsafeCast<ArrayBuffer>()
                                        ?: error("Could not read blob")

                                    val bytes = Uint8Array(arrayBuffer)
                                    val byteArray = ByteArray(bytes.length)
                                    for (i in 0 until bytes.length) {
                                        byteArray[i] = bytes[i]
                                    }

                                    continuation.resume(byteArray)
                                } catch (e: Exception) {
                                    continuation.resumeWithException(e)
                                }
                            }
                            reader.readAsArrayBuffer(blob)
                        } else {
                            continuation.resumeWithException(Exception("Failed to create blob"))
                        }
                    }, "image/jpeg", quality)
                } catch (e: Exception) {
                    continuation.resumeWithException(e)
                }
            }

            setImageOnError(img) {
                URL.revokeObjectURL(objectUrl)
                continuation.resumeWithException(Exception("Failed to load image"))
            }

            img.src = objectUrl
        } catch (e: Exception) {
            continuation.resumeWithException(e)
        }
    }
}

private fun calculateResizeDimensions(
    inWidth: Int,
    inHeight: Int,
    maxSize: Int,
): Pair<Int, Int> {
    val outWidth = if (inWidth > inHeight) maxSize else inWidth * maxSize / inHeight
    val outHeight = if (inWidth > inHeight) inHeight * maxSize / inWidth else maxSize
    return outWidth to outHeight
}
