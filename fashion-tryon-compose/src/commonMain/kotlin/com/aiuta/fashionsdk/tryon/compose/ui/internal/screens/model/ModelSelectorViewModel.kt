package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPickerEventType
import com.aiuta.fashionsdk.configuration.features.picker.model.AiutaImagePickerPredefinedModelFeature
import com.aiuta.fashionsdk.logger.AiutaLogger
import com.aiuta.fashionsdk.logger.d
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.LastSavedImages
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel.TryOnModelGenderUiModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel.TryOnModelUiModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel.toGenders
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel.toUrlImage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendPickerAnalytic
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.activateAutoTryOn
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.data.AiutaTryOnDataController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.data.provideTryOnModels
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.models.GenderTabUiModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.models.ModelSelectorScreenAction
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.models.ModelSelectorScreenEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.models.ModelSelectorScreenViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ModelSelectorViewModel(
    private val predefinedModelFeature: AiutaImagePickerPredefinedModelFeature,
    private val dataController: AiutaTryOnDataController,
    private val controller: FashionTryOnController,
    private val logger: AiutaLogger?,
) : ViewModel() {

    // Full per-gender data lives here, not in the ViewState
    private var gendersById: Map<String, TryOnModelGenderUiModel> = emptyMap()

    private val _viewState = MutableStateFlow(ModelSelectorScreenViewState())
    val viewState = _viewState.asStateFlow()

    private val _viewAction = MutableStateFlow<ModelSelectorScreenAction?>(null)
    val viewAction = _viewAction.asStateFlow()

    init {
        loadModels()
    }

    fun obtainEvent(event: ModelSelectorScreenEvent) {
        when (event) {
            is ModelSelectorScreenEvent.BackClicked -> {
                _viewAction.update { ModelSelectorScreenAction.NavigateBack }
            }

            is ModelSelectorScreenEvent.GenderSelected -> selectGender(event.genderId)

            is ModelSelectorScreenEvent.ActiveModelChanged -> changeActiveModel(event.model)

            is ModelSelectorScreenEvent.TryOnClicked -> tryOnActiveModel()

            is ModelSelectorScreenEvent.RetryClicked -> loadModels(forceUpdate = true)
        }
    }

    fun clearAction() {
        _viewAction.value = null
    }

    private fun loadModels(forceUpdate: Boolean = false) {
        viewModelScope.launch {
            _viewState.update { it.copy(status = ModelSelectorScreenViewState.Status.LOADING) }

            dataController
                .provideTryOnModels(forceUpdate = forceUpdate)
                .onFailure { error ->
                    logger?.d("ModelSelectorViewModel: failed to load try on models - $error")
                    _viewState.update { it.copy(status = ModelSelectorScreenViewState.Status.GENERAL_ERROR) }
                }
                .onSuccess { models ->
                    // Map the flat list into the gender dimension
                    val genders = models.toGenders(
                        predefinedModelCategories = predefinedModelFeature.strings.predefinedModelCategories,
                    )

                    gendersById = genders.associateBy { it.id }

                    if (genders.isEmpty()) {
                        _viewState.update {
                            it.copy(status = ModelSelectorScreenViewState.Status.EMPTY_MODELS_ERROR)
                        }
                    } else {
                        val preferredGenderId = predefinedModelFeature.data?.preferredCategoryId
                        val activeGender = genders
                            .firstOrNull { it.id == preferredGenderId }
                            ?: genders.first()

                        _viewState.update {
                            it.copy(
                                status = ModelSelectorScreenViewState.Status.CONTENT,
                                genders = genders.map { gender -> GenderTabUiModel(id = gender.id, title = gender.title) },
                                activeGender = activeGender,
                                activeModel = activeGender.models.firstOrNull(),
                            )
                        }
                    }
                }
        }
    }

    private fun selectGender(genderId: String) {
        val gender = gendersById[genderId] ?: return
        _viewState.update {
            it.copy(
                activeGender = gender,
                activeModel = gender.models.firstOrNull(),
            )
        }
    }

    private fun changeActiveModel(model: TryOnModelUiModel?) {
        _viewState.update { it.copy(activeModel = model) }
    }

    private fun tryOnActiveModel() {
        val model = _viewState.value.activeModel ?: return

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
        _viewAction.update { ModelSelectorScreenAction.NavigateToImageSelector }
    }
}
