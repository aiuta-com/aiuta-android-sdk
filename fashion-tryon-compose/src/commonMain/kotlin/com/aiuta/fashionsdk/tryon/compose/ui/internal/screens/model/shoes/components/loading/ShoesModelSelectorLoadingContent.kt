package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.shoes.components.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.placeholderFadeConnecting

/**
 * SHOES mode loading skeleton — mirrors the [shoesModelsBlock] layout: a couple of placeholder
 * title bars, each followed by a row of placeholder thumbnail boxes.
 */
@Composable
internal fun ShoesModelSelectorLoadingContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        repeat(BLOCK_COUNT) {
            // Title placeholder
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp, bottom = 8.dp)
                    .width(140.dp)
                    .height(20.dp)
                    .placeholderFadeConnecting(shapeDp = 8.dp),
            )

            // Thumbnails placeholder
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                repeat(THUMBNAIL_COUNT) {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(138.dp)
                            .placeholderFadeConnecting(shapeDp = 8.dp),
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

private const val BLOCK_COUNT = 2
private const val THUMBNAIL_COUNT = 4
