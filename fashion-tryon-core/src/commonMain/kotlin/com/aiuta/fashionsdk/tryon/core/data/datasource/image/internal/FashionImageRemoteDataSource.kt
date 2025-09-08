package com.aiuta.fashionsdk.tryon.core.data.datasource.image.internal

import com.aiuta.fashionsdk.network.NetworkClient
import com.aiuta.fashionsdk.network.paging.models.PageContainer
import com.aiuta.fashionsdk.network.paging.models.PaginationOffset
import com.aiuta.fashionsdk.network.paging.utils.saveAppend
import com.aiuta.fashionsdk.network.utils.saveAppendLimit
import com.aiuta.fashionsdk.tryon.core.data.datasource.image.FashionImageDataSource
import com.aiuta.fashionsdk.tryon.core.data.datasource.image.models.UploadedImage
import com.aiuta.fashionsdk.tryon.core.utils.extension
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

internal class FashionImageRemoteDataSource(
    private val networkClient: NetworkClient,
) : FashionImageDataSource {
    override suspend fun createUploadedImage(
        fileName: String,
        fileByteArray: ByteArray,
    ): UploadedImage = networkClient.httpClient.value.submitFormWithBinaryData(
        url = PATH_UPLOADED_IMAGES,
        formData =
        formData {
            append(
                key = KEY_IMAGE_DATA,
                value = fileByteArray,
                headers =
                Headers.build {
                    append(HttpHeaders.ContentType, "image/${fileName.extension}")
                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                },
            )
        },
    ).body()

    override suspend fun getUploadedImages(
        paginationOffset: PaginationOffset?,
        paginationLimit: Int?,
    ): PageContainer<UploadedImage> = networkClient.httpClient.value.get(
        urlString = PATH_UPLOADED_IMAGES,
    ) {
        url {
            saveAppend(paginationOffset)
            saveAppendLimit(paginationLimit)
        }
    }.body()

    override suspend fun getUploadedImage(imageId: String): UploadedImage = networkClient.httpClient.value.get(
        urlString = "$PATH_UPLOADED_IMAGES/$imageId",
    ).body()

    override suspend fun deleteUploadedImage(imageId: String): Boolean = networkClient.httpClient.value.delete(
        urlString = "$PATH_UPLOADED_IMAGES/$imageId",
    ).body()

    private companion object {
        const val PATH_UPLOADED_IMAGES = "/uploaded_images"
        const val KEY_IMAGE_DATA = "image_data"
    }
}
