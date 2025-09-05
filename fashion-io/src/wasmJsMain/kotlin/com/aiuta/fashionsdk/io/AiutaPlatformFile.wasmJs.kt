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
import org.w3c.files.File
import org.w3c.files.FileReader

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
): ByteArray {
    // TODO Make compressed byte array
    return readByteArray(platformContext = platformContext)
}
