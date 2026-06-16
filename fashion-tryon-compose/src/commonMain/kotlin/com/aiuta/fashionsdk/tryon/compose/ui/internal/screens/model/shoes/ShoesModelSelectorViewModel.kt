package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.shoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPickerEventType
import com.aiuta.fashionsdk.configuration.features.picker.model.AiutaImagePickerPredefinedModelFeature
import com.aiuta.fashionsdk.configuration.mode.shoes.AiutaShoesMode
import com.aiuta.fashionsdk.logger.AiutaLogger
import com.aiuta.fashionsdk.logger.d
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.LastSavedImages
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel.ShoesModelGenderUiModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel.TryOnModelUiModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel.toShoesGenders
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel.toUrlImage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendPickerAnalytic
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.activateAutoTryOn
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.data.AiutaTryOnDataController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.data.provideTryOnModels
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.general.models.GenderTabUiModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.shoes.models.ShoesModelSelectorScreenAction
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.shoes.models.ShoesModelSelectorScreenEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.shoes.models.ShoesModelSelectorScreenViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ShoesModelSelectorViewModel(
    private val predefinedModelFeature: AiutaImagePickerPredefinedModelFeature,
    private val shoesMode: AiutaShoesMode?,
    private val dataController: AiutaTryOnDataController,
    private val controller: FashionTryOnController,
    private val logger: AiutaLogger?,
) : ViewModel() {

    private var gendersById: Map<String, ShoesModelGenderUiModel> = emptyMap()

    private val _viewState = MutableStateFlow(ShoesModelSelectorScreenViewState())
    val viewState = _viewState.asStateFlow()

    private val _viewAction = MutableStateFlow<ShoesModelSelectorScreenAction?>(null)
    val viewAction = _viewAction.asStateFlow()

    init {
        loadModels()
    }

    fun obtainEvent(event: ShoesModelSelectorScreenEvent) {
        when (event) {
            is ShoesModelSelectorScreenEvent.BackClicked -> {
                _viewAction.update { ShoesModelSelectorScreenAction.NavigateBack }
            }

            is ShoesModelSelectorScreenEvent.GenderSelected -> selectGender(event.genderId)

            is ShoesModelSelectorScreenEvent.ModelClicked -> tryOnModel(event.model)

            is ShoesModelSelectorScreenEvent.RetryClicked -> loadModels(forceUpdate = true)
        }
    }

    fun clearAction() {
        _viewAction.value = null
    }

    private fun loadModels(forceUpdate: Boolean = false) {
        viewModelScope.launch {
            _viewState.update { it.copy(status = ShoesModelSelectorScreenViewState.Status.LOADING) }

            dataController
                .provideTryOnModels(forceUpdate = forceUpdate)
                .onFailure { error ->
                    logger?.d("ShoesModelSelectorViewModel: failed to load try on models - $error")
                    _viewState.update { it.copy(status = ShoesModelSelectorScreenViewState.Status.GENERAL_ERROR) }
                }
                .onSuccess { models ->
                    val genders = models.toShoesGenders(
                        predefinedModelCategories = predefinedModelFeature.strings.predefinedModelCategories,
                        shoesMode = shoesMode,
                    )

                    if (genders.isEmpty()) {
                        _viewState.update {
                            it.copy(status = ShoesModelSelectorScreenViewState.Status.EMPTY_MODELS_ERROR)
                        }
                    } else {
                        val preferredGenderId = predefinedModelFeature.data?.preferredCategoryId
                        val orderedGenders = genders.sortedByDescending { it.id == preferredGenderId }
                        val activeGender = orderedGenders.first()

                        gendersById = orderedGenders.associateBy { it.id }

                        _viewState.update {
                            it.copy(
                                status = ShoesModelSelectorScreenViewState.Status.CONTENT,
                                genders = orderedGenders.map { gender -> GenderTabUiModel(id = gender.id, title = gender.title) },
                                activeGender = activeGender,
                            )
                        }
                    }
                }
        }
    }

    private fun selectGender(genderId: String) {
        val gender = gendersById[genderId] ?: return
        _viewState.update { it.copy(activeGender = gender) }
    }

    private fun tryOnModel(model: TryOnModelUiModel) {
        controller.sendPickerAnalytic(
            event = AiutaAnalyticsPickerEventType.PREDEFINED_MODEL_SELECTED,
            pageId = AiutaAnalyticsPageId.IMAGE_PICKER,
        )

        // Save model
        controller.lastSavedImages.value = LastSavedImages.UrlSource.PregeneratedModels(
            urlImages = listOf(model.toUrlImage()),
        )

        // Activate try on
        controller.activateAutoTryOn()

        // Go back to picker
        _viewAction.update { ShoesModelSelectorScreenAction.NavigateToImageSelector }
    }
}
