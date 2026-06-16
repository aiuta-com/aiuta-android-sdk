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

internal const val TAG_GENDER = "gender"
internal const val TAG_VIEW = "view"

// The single `view` shown in the GENERAL flow
internal const val VIEW_GENERAL = "full-height"

/**
 * Builds the gender dimension for the GENERAL model selector from a flat list of models.
 *
 * Keeps only models with `view == `[VIEW_GENERAL], then groups them by their `gender` tag.
 * [predefinedModelCategories] is used purely to resolve a gender's title — genders without a tag
 * or without a matching label are skipped (no invented fallbacks). Gender order follows the models.
 */
internal fun List<TryOnModelUiModel>.toGenders(
    predefinedModelCategories: Map<String, String>,
): List<TryOnModelGenderUiModel> = this
    .filter { model -> model.tags[TAG_VIEW] == VIEW_GENERAL }
    .groupBy { model -> model.tags[TAG_GENDER] }
    .mapNotNull { (genderId, genderModels) ->
        val title = genderId?.let(predefinedModelCategories::get) ?: return@mapNotNull null
        TryOnModelGenderUiModel(
            id = genderId,
            title = title,
            models = genderModels,
        )
    }
