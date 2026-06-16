package com.aiuta.fashionsdk.tryon.core.data.datasource.image

import com.aiuta.fashionsdk.tryon.core.data.datasource.image.models.UploadedImage

internal interface FashionImageDataSource {
    suspend fun createUploadedImage(
        fileName: String,
        fileByteArray: ByteArray,
    ): UploadedImage
}
