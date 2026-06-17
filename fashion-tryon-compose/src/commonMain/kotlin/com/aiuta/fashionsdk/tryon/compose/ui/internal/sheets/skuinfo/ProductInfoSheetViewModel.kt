package com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.skuinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.configuration.features.AiutaFeatures
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem
import com.aiuta.fashionsdk.configuration.features.wishlist.AiutaWishlistFeature
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.zoom.ZoomImageUiModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.clickAddToWishListActiveSKU
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.controller.openScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.skuinfo.models.ProductInfoSheetEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.skuinfo.models.ProductInfoSheetViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ProductInfoSheetViewModel(
    private val features: AiutaFeatures,
    private val controller: FashionTryOnController,
    private val productItem: ProductItem,
    private val originPageId: AiutaAnalyticsPageId,
) : ViewModel() {

    private val wishlistFeature = features.provideFeature<AiutaWishlistFeature>()

    private val _viewState = MutableStateFlow(buildInitialState())
    val viewState = _viewState.asStateFlow()

    init {
        observeWishlist()
    }

    fun obtainEvent(event: ProductInfoSheetEvent) {
        when (event) {
            is ProductInfoSheetEvent.WishlistToggled -> toggleWishlist()
            is ProductInfoSheetEvent.ImageClicked -> openZoom(event)
        }
    }

    private fun buildInitialState(): ProductInfoSheetViewState {
        val wishlistProductIds = wishlistFeature?.dataProvider?.wishlistProductIds?.value.orEmpty()
        return ProductInfoSheetViewState(
            isWishlistAvailable = wishlistFeature != null,
            isInWishlist = productItem.id in wishlistProductIds,
        )
    }

    /** Reflects the wishlist membership of this product, mirroring the result screen's heart state. */
    private fun observeWishlist() {
        val feature = wishlistFeature ?: return
        viewModelScope.launch {
            feature.dataProvider.wishlistProductIds.collect { wishlistProductIds ->
                _viewState.update { it.copy(isInWishlist = productItem.id in wishlistProductIds) }
            }
        }
    }

    private fun toggleWishlist() {
        val feature = wishlistFeature ?: return
        controller.clickAddToWishListActiveSKU(
            pageId = originPageId,
            productIds = listOf(productItem.id),
            updatedWishlistState = !_viewState.value.isInWishlist,
            dataProvider = feature.dataProvider,
        )
    }

    private fun openZoom(event: ProductInfoSheetEvent.ImageClicked) {
        controller.zoomImageController.openScreen(
            model = ZoomImageUiModel(
                imageSize = event.imageSize,
                initialCornerRadius = event.cornerRadius,
                imageUrl = event.imageUrl,
                parentImageOffset = event.parentOffset,
                // Preserve the original zoom analytics origin.
                originPageId = AiutaAnalyticsPageId.RESULTS,
            ),
        )
    }
}
