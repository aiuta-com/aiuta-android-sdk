package com.aiuta.fashionsdk.tryon.compose.ui.internal.components.images

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.aiuta.fashionsdk.compose.tokens.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.progress.ErrorProgress
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.progress.LoadingProgress

@Composable
internal fun ImagesContainer(
    modifier: Modifier = Modifier,
    getImageUrls: () -> List<String>,
) {
    val imageUrls = getImageUrls()

    when {
        imageUrls.size <= 2 -> {
            ImagesContainerSmall(
                modifier = modifier,
                getImageUrls = getImageUrls,
            )
        }

        imageUrls.size == 3 -> {
            ImagesContainerMedium(
                modifier = modifier,
                getImageUrls = getImageUrls,
            )
        }

        else -> {
            ImagesContainerBig(
                modifier = modifier,
                getImageUrls = getImageUrls,
            )
        }
    }
}

@Composable
private fun ImagesContainerSmall(
    modifier: Modifier = Modifier,
    getImageUrls: () -> List<String>,
) {
    val context = LocalContext.current
    val imageUrls = getImageUrls()

    SubcomposeAsyncImage(
        modifier = modifier,
        model =
            ImageRequest.Builder(context)
                .data(imageUrls.firstOrNull())
                .build(),
        contentDescription = null,
        loading = { LoadingProgress(modifier = Modifier.fillMaxSize()) },
        error = { ErrorProgress(modifier = Modifier.fillMaxSize()) },
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun ImagesContainerMedium(
    modifier: Modifier = Modifier,
    getImageUrls: () -> List<String>,
) {
    val context = LocalContext.current
    val theme = LocalTheme.current
    val imageUrls = getImageUrls()

    Row(
        modifier = modifier.background(theme.colors.background),
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier.fillMaxHeight().weight(1f),
            model =
                ImageRequest.Builder(context)
                    .data(imageUrls.firstOrNull())
                    .build(),
            contentDescription = null,
            loading = { LoadingProgress(modifier = Modifier.fillMaxSize()) },
            error = { ErrorProgress(modifier = Modifier.fillMaxSize()) },
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier.fillMaxHeight().weight(1f),
        ) {
            for (index in 1..imageUrls.lastIndex) {
                SubcomposeAsyncImage(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    model =
                        ImageRequest.Builder(context)
                            .data(imageUrls.getOrNull(index))
                            .build(),
                    contentDescription = null,
                    loading = { LoadingProgress(modifier = Modifier.fillMaxSize()) },
                    error = { ErrorProgress(modifier = Modifier.fillMaxSize()) },
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

@Composable
private fun ImagesContainerBig(
    modifier: Modifier = Modifier,
    getImageUrls: () -> List<String>,
) {
    val context = LocalContext.current
    val theme = LocalTheme.current
    val imageUrls = getImageUrls()

    val rowSize = 2
    val columnSize = 2

    Row(
        modifier = modifier.background(theme.colors.background),
    ) {
        (0 until rowSize).forEach { rowIndex ->
            Column(
                modifier = Modifier.weight(1f).fillMaxHeight(),
            ) {
                (0 until columnSize).forEach { columnIndex ->
                    SubcomposeAsyncImage(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        model =
                            ImageRequest.Builder(context)
                                .data(imageUrls.getOrNull(rowIndex * rowSize + columnIndex))
                                .build(),
                        contentDescription = null,
                        loading = { LoadingProgress(modifier = Modifier.fillMaxSize()) },
                        error = { ErrorProgress(modifier = Modifier.fillMaxSize()) },
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
    }
}
