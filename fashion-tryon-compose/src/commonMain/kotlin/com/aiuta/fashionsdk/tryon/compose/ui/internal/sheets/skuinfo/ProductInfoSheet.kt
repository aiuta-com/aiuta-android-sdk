package com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.skuinfo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiuta.fashionsdk.compose.uikit.composition.LocalAiutaFeatures
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnBottomSheetScreen

@Composable
internal fun ProductInfoSheet(
    productInfo: TryOnBottomSheetScreen.ProductInfo,
    modifier: Modifier = Modifier,
) {
    val controller = LocalController.current
    val features = LocalAiutaFeatures.current

    val viewModel = viewModel(key = productInfo.productItem.id) {
        ProductInfoSheetViewModel(
            features = features,
            controller = controller,
            productItem = productInfo.productItem,
            originPageId = productInfo.originPageId,
        )
    }

    val viewState = viewModel.viewState.collectAsStateWithLifecycle()

    ProductInfoSheetContent(
        productInfo = productInfo,
        viewState = viewState,
        eventHandler = viewModel::obtainEvent,
        modifier = modifier,
    )
}
