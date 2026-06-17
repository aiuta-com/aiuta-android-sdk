package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.blocks.addtocart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.button.FashionButton
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonSizes
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonStyles
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models.GenerationResultScreenEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models.GenerationResultScreenViewState

internal fun LazyListScope.addToCartBlock(
    viewState: State<GenerationResultScreenViewState>,
    eventHandler: (GenerationResultScreenEvent) -> Unit,
) {
    item(
        key = "add_to_cart_block",
        contentType = "add_to_cart_block",
    ) {
        val theme = LocalTheme.current

        FashionButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(top = 10.dp, bottom = 14.dp),
            text = viewState.value.addToCartText,
            style = FashionButtonStyles.primaryStyle(theme),
            size = FashionButtonSizes.lSize(),
            onClick = {
                eventHandler(GenerationResultScreenEvent.AddToCartClicked)
            },
        )
    }
}
