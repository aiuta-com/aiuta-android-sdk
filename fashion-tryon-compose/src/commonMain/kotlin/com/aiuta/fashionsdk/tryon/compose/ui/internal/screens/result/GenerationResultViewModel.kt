package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsResultsEventType
import com.aiuta.fashionsdk.configuration.features.AiutaFeatures
import com.aiuta.fashionsdk.configuration.features.share.AiutaShareFeature
import com.aiuta.fashionsdk.configuration.features.tryon.AiutaTryOnFeature
import com.aiuta.fashionsdk.configuration.features.tryon.cart.AiutaTryOnCartFeature
import com.aiuta.fashionsdk.configuration.features.tryon.cart.outfit.AiutaTryOnCartOutfitFeature
import com.aiuta.fashionsdk.configuration.features.tryon.disclaimer.AiutaTryOnFitDisclaimerFeature
import com.aiuta.fashionsdk.configuration.features.tryon.feedback.AiutaTryOnFeedbackFeature
import com.aiuta.fashionsdk.configuration.features.tryon.other.AiutaTryOnWithOtherPhotoFeature
import com.aiuta.fashionsdk.configuration.features.wishlist.AiutaWishlistFeature
import com.aiuta.fashionsdk.logger.AiutaLogger
import com.aiuta.fashionsdk.logger.d
import com.aiuta.fashionsdk.logger.i
import com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.generated.operations.GeneratedOperationInteractor
import com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.session.SessionGenerationInteractor
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.SessionImageUIModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.zoom.ZoomImageUiModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.clickAddOutfitToCart
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.clickAddProductToCart
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.clickAddToWishListActiveSKU
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.controller.openScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.analytic.sendLikeGenerationFeedback
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.analytic.sendResultEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models.GenerationResultScreenAction
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models.GenerationResultScreenEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models.GenerationResultScreenViewState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.utils.resolveAddToCartText
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val THANKS_FEEDBACK_SHOW_DELAY = 3000L
private const val TAG = "GenerationResultViewModel"

internal class GenerationResultViewModel(
    private val features: AiutaFeatures,
    private val controller: FashionTryOnController,
    private val sessionGenerationInteractor: SessionGenerationInteractor,
    private val generatedOperationInteractor: GeneratedOperationInteractor,
    private val logger: AiutaLogger?,
) : ViewModel() {

    private val _viewState = MutableStateFlow(buildInitialState())
    val viewState = _viewState.asStateFlow()

    private val _viewAction = MutableStateFlow<GenerationResultScreenAction?>(null)
    val viewAction = _viewAction.asStateFlow()

    private var thanksFeedbackJob: Job? = null

    init {
        val initialState = _viewState.value
        logger?.i(
            "init(): opened with ${initialState.generations.size} generation(s) and " +
                "${initialState.products.size} product(s)",
            tag = TAG,
        )

        observeSessionGenerations()
        observeGeneratedOperations()
        observeWishlist()
    }

    fun obtainEvent(event: GenerationResultScreenEvent) {
        when (event) {
            is GenerationResultScreenEvent.PhotoClicked -> openZoom(event)
            is GenerationResultScreenEvent.WishlistToggled -> toggleWishlist(event.sessionImage, event.isAdding)
            is GenerationResultScreenEvent.LikeClicked -> onLikeClicked(event.sessionImage)
            is GenerationResultScreenEvent.DislikeClicked -> onDislikeClicked(event.sessionImage)
            is GenerationResultScreenEvent.ChangePhotoClicked -> onChangePhotoClicked(event.sessionImage)
            is GenerationResultScreenEvent.FeedbackProvidedViaSheet -> showThanksFeedback()
            is GenerationResultScreenEvent.AddToCartClicked -> addToCart()
            is GenerationResultScreenEvent.ProductRowClicked -> {
                _viewAction.update { GenerationResultScreenAction.ShowProductInfoSheet(event.product) }
            }
            is GenerationResultScreenEvent.FitDisclaimerClicked -> {
                _viewAction.update { GenerationResultScreenAction.ShowFitDisclaimerSheet }
            }
        }
    }

    fun clearAction() {
        _viewAction.value = null
    }

    private fun buildInitialState(): GenerationResultScreenViewState {
        val tryOnFeature = features.strictProvideFeature<AiutaTryOnFeature>()
        val fitDisclaimerFeature = features.provideFeature<AiutaTryOnFitDisclaimerFeature>()

        val products = controller.activeProductItems.toList()
        val isSingleMode = products.size == 1

        return GenerationResultScreenViewState(
            title = tryOnFeature.strings.tryOnPageTitle,
            generations = sessionGenerationInteractor.sessionGenerations.toList(),
            products = products,
            addToCartText = features.resolveAddToCartText(isSingleMode = isSingleMode),
            isShareAvailable = features.provideFeature<AiutaShareFeature>() != null,
            isWishlistAvailable = features.provideFeature<AiutaWishlistFeature>() != null,
            isFeedbackAvailable = features.provideFeature<AiutaTryOnFeedbackFeature>() != null,
            isGenerateMoreAvailable = features.provideFeature<AiutaTryOnWithOtherPhotoFeature>() != null,
            fitDisclaimerText = fitDisclaimerFeature?.strings?.tryOnFitTitle,
        )
    }

    /**
     * Mirrors the old `GenerationResultSessionListener`: keep the pager in sync with the session and
     * navigate back to the picker once every session image has been deleted.
     */
    private fun observeSessionGenerations() {
        viewModelScope.launch {
            snapshotFlow { sessionGenerationInteractor.sessionGenerations.toList() }
                .collect { generations ->
                    if (generations.isEmpty()) {
                        logger?.i("observeSessionGenerations(): no session images left, navigating back", tag = TAG)
                        _viewAction.update { GenerationResultScreenAction.NavigateBack }
                    } else {
                        _viewState.update { it.copy(generations = generations) }
                    }
                }
        }
    }

    private fun observeGeneratedOperations() {
        viewModelScope.launch {
            generatedOperationInteractor.countGeneratedOperation().collect { count ->
                _viewState.update { it.copy(generatedOperationsCount = count) }
            }
        }
    }

    /** Ports `AiutaWishlistFeature.inWishlistListener()` into the view model. */
    private fun observeWishlist() {
        val wishlistFeature = features.provideFeature<AiutaWishlistFeature>() ?: return
        val activeProductItemsIds = controller.activeProductItemsIds
        viewModelScope.launch {
            wishlistFeature.dataProvider.wishlistProductIds.collect { wishlistProductIds ->
                _viewState.update {
                    it.copy(isActiveProductInWishlist = wishlistProductIds.containsAll(activeProductItemsIds))
                }
            }
        }
    }

    private fun openZoom(event: GenerationResultScreenEvent.PhotoClicked) {
        controller.zoomImageController.openScreen(
            model = ZoomImageUiModel(
                imageSize = event.imageSize,
                initialCornerRadius = event.cornerRadius,
                imageUrl = event.sessionImage.imageUrl,
                parentImageOffset = event.parentOffset,
                originPageId = AiutaAnalyticsPageId.RESULTS,
            ),
        )
    }

    private fun toggleWishlist(sessionImage: SessionImageUIModel, isAdding: Boolean) {
        val wishlistFeature = features.provideFeature<AiutaWishlistFeature>() ?: return
        controller.clickAddToWishListActiveSKU(
            pageId = AiutaAnalyticsPageId.RESULTS,
            updatedWishlistState = isAdding,
            dataProvider = wishlistFeature.dataProvider,
            productIds = sessionImage.productIds,
        )
    }

    private fun onLikeClicked(sessionImage: SessionImageUIModel) {
        logger?.d("onLikeClicked(): positive feedback for image ${sessionImage.id}", tag = TAG)
        controller.sendLikeGenerationFeedback(sessionImage)
        sessionGenerationInteractor.setFeedbackAsProvided(sessionImage)
        showThanksFeedback()
    }

    private fun onDislikeClicked(sessionImage: SessionImageUIModel) {
        logger?.d("onDislikeClicked(): opening feedback sheet for image ${sessionImage.id}", tag = TAG)
        _viewAction.update { GenerationResultScreenAction.ShowFeedbackSheet(sessionImage.productIds) }
        sessionGenerationInteractor.setFeedbackAsProvided(sessionImage)
    }

    private fun onChangePhotoClicked(sessionImage: SessionImageUIModel) {
        logger?.i(
            "onChangePhotoClicked(): generatedOperationsCount=${_viewState.value.generatedOperationsCount}",
            tag = TAG,
        )
        controller.sendResultEvent(
            event = AiutaAnalyticsResultsEventType.PICK_OTHER_PHOTO,
            pageId = AiutaAnalyticsPageId.RESULTS,
            productIds = sessionImage.productIds,
        )
        _viewAction.update {
            GenerationResultScreenAction.ShowChangePhotoSheet(
                hasMultipleOperations = _viewState.value.generatedOperationsCount > 1,
            )
        }
    }

    private fun addToCart() {
        val products = _viewState.value.products
        val isSingleMode = products.size == 1
        val cartFeature = features.provideFeature<AiutaTryOnCartFeature>()
        val outfitFeature = features.provideFeature<AiutaTryOnCartOutfitFeature>()

        when {
            !isSingleMode && outfitFeature != null -> {
                logger?.i("addToCart(): adding outfit of ${products.size} products to cart", tag = TAG)
                controller.clickAddOutfitToCart(
                    pageId = AiutaAnalyticsPageId.RESULTS,
                    handler = outfitFeature.handler,
                )
            }

            cartFeature != null -> {
                val productId = products.firstOrNull()?.id ?: return
                logger?.i("addToCart(): adding product $productId to cart", tag = TAG)
                controller.clickAddProductToCart(
                    pageId = AiutaAnalyticsPageId.RESULTS,
                    productId = productId,
                    handler = cartFeature.handler,
                )
            }
        }
    }

    /** Ports `GenerationResultController.showThanksFeedbackBlock()`. */
    private fun showThanksFeedback() {
        if (_viewState.value.isThanksFeedbackVisible) return
        thanksFeedbackJob = viewModelScope.launch {
            _viewState.update { it.copy(isThanksFeedbackVisible = true) }
            delay(THANKS_FEEDBACK_SHOW_DELAY)
            _viewState.update { it.copy(isThanksFeedbackVisible = false) }
        }
    }
}
