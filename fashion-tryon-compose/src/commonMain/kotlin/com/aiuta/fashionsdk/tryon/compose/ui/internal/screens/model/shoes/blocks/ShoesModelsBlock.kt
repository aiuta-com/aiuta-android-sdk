package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.shoes.blocks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.configuration.ui.theme.AiutaTheme
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel.ModelView
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel.ShoesModelGenderUiModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.shoes.models.ShoesModelSelectorScreenEvent

internal fun LazyListScope.shoesModelsBlock(
    activeGender: ShoesModelGenderUiModel,
    theme: AiutaTheme,
    eventHandler: (ShoesModelSelectorScreenEvent) -> Unit,
) {
    items(
        items = activeGender.viewBlocks,
        key = { viewBlock -> viewBlock.view },
        contentType = { "SHOES_VIEW_BLOCK" },
    ) { viewBlock ->
        val isFullHeight = viewBlock.view == ModelView.FULL_HEIGHT.id
        val itemWidth = if (isFullHeight) 170.dp else 123.dp
        val itemHeight = if (isFullHeight) 265.dp else 192.dp
        val sharedShape = RoundedCornerShape(if (isFullHeight) 24.dp else 17.dp)

        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 14.dp),
                text = viewBlock.title,
                style = theme.productBar.typography.product,
                color = theme.color.secondary,
            )

            Spacer(Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    items = viewBlock.models,
                    key = { model -> model.id },
                    contentType = { "SHOES_VIEW_BLOCK_ITEM" },
                ) { model ->
                    AiutaImage(
                        modifier = Modifier
                            .width(itemWidth)
                            .height(itemHeight)
                            .clip(sharedShape)
                            .clickable {
                                eventHandler(ShoesModelSelectorScreenEvent.ModelClicked(model))
                            },
                        imageUrl = model.url,
                        shape = sharedShape,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
    }
}
