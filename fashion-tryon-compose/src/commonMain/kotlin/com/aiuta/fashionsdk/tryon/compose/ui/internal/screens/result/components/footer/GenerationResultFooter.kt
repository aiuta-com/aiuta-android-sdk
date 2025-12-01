package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.footer

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.isSingleTryOnMode
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.footer.blocks.common.spacerBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.footer.blocks.description.itemDescriptionBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.footer.blocks.lists.activeProductsBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.footer.blocks.lists.itemPhotosBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.footer.blocks.outfit.outfitCartBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.footer.blocks.outfit.outfitTitleBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.controller.GenerationResultController

internal const val FOOTER_FULL_SIZE_SPAN = 2

@Composable
internal fun GenerationResultFooterList(
    modifier: Modifier = Modifier,
    generationResultController: GenerationResultController,
) {
    val controller = LocalController.current
    val density = LocalDensity.current

    val statusBarsPx = WindowInsets.statusBars.getTop(density)
    val statusBars = with(density) { statusBarsPx.toDp() }

    val isSingleTryOnMode = controller.isSingleTryOnMode()

    LazyVerticalGrid(
        modifier = modifier,
        state = generationResultController.footerListState,
        columns = GridCells.Fixed(FOOTER_FULL_SIZE_SPAN),
    ) {
        spacerBlock(index = 0, height = 20.dp)

        when (isSingleTryOnMode.value) {
            true -> footerSingleTryOnContent(
                generationResultController = generationResultController,
            )

            false -> footerMultiTryOnContent(
                generationResultController = generationResultController,
            )
        }

        spacerBlock(index = 2, height = statusBars)
    }
}

private fun LazyGridScope.footerSingleTryOnContent(
    generationResultController: GenerationResultController,
) {
    itemDescriptionBlock(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    )

    spacerBlock(index = 1, height = 32.dp)

    itemPhotosBlock(
        modifier = Modifier.fillMaxWidth(),
        generationResultController = generationResultController,
    )
}

private fun LazyGridScope.footerMultiTryOnContent(
    generationResultController: GenerationResultController,
) {
    outfitTitleBlock(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp),
    )

    spacerBlock(index = 1, height = 20.dp)

    activeProductsBlock(
        modifier = Modifier.fillMaxWidth(),
        generationResultController = generationResultController,
    )

    outfitCartBlock(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    )
}
