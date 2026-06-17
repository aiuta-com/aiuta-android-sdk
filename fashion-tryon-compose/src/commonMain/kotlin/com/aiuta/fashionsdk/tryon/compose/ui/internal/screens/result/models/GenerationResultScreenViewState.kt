package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.SessionImageUIModel

@Immutable
internal data class GenerationResultScreenViewState(
    val title: String,
    // Pager pages
    val generations: List<SessionImageUIModel>,
    // Products used for the generation (the SKU list)
    val products: List<ProductItem>,
    // Observed flows
    val generatedOperationsCount: Int = 0,
    val isActiveProductInWishlist: Boolean = false,
    // Like-confirmation overlay
    val isThanksFeedbackVisible: Boolean = false,
    // Resolved add-to-cart button text (single product vs outfit)
    val addToCartText: String,
    // Feature flags
    val isShareAvailable: Boolean,
    val isWishlistAvailable: Boolean,
    val isFeedbackAvailable: Boolean,
    val isGenerateMoreAvailable: Boolean,
    // Non-null only when AiutaTryOnFitDisclaimerFeature is enabled
    val fitDisclaimerText: String?,
)
