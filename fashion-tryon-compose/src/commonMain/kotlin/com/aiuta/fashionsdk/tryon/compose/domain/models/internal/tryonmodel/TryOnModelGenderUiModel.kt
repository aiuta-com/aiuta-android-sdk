package com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel

import androidx.compose.runtime.Immutable

/**
 * Top-level dimension of the model selector — the gender toggle shown in the app bar.
 *
 * [id] is the raw `gender` tag value (e.g. "female"), [title] its localized label resolved from
 * `predefinedModelCategories`, [models] the gender's models for the GENERAL flow (filtered to the
 * general `view`).
 */
@Immutable
internal data class TryOnModelGenderUiModel(
    val id: String,
    val title: String,
    val models: List<TryOnModelUiModel>,
)

// The single `view` shown in the GENERAL flow
private val VIEW_GENERAL = ModelView.FULL_HEIGHT

/**
 * Builds the gender dimension for the GENERAL model selector from a flat list of models.
 *
 * Keeps only models with `view == `[VIEW_GENERAL], then builds one tab per [Gender.selectorGenders]
 * value, folding [Gender.UNISEX] models into both. [predefinedModelCategories] resolves a gender's
 * title — genders without a matching label or without any models are skipped (no invented
 * fallbacks).
 */
internal fun List<TryOnModelUiModel>.toGenders(
    predefinedModelCategories: Map<String, String>,
): List<TryOnModelGenderUiModel> {
    val generalModels = filter { model -> model.tags.view == VIEW_GENERAL }

    return Gender.selectorGenders.mapNotNull { gender ->
        val title = predefinedModelCategories[gender.id] ?: return@mapNotNull null
        val models = generalModels.filter { model -> model.tags.gender.matches(gender) }
        if (models.isEmpty()) return@mapNotNull null
        TryOnModelGenderUiModel(
            id = gender.id,
            title = title,
            models = models,
        )
    }
}
