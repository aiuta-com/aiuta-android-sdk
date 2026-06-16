package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.models

import androidx.compose.runtime.Immutable

/**
 * Lightweight descriptor for the gender toggle — only what the tabs need to render.
 * The full per-gender content lives in the ViewModel, not in the ViewState.
 */
@Immutable
internal data class GenderTabUiModel(
    val id: String,
    val title: String,
)
