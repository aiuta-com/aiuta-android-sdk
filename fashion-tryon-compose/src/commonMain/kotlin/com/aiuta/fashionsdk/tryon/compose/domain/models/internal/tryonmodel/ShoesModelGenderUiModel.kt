package com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.configuration.mode.shoes.AiutaShoesMode

/**
 * Top-level dimension of the SHOES model selector — the gender toggle shown in the app bar.
 *
 * [id] is the raw `gender` tag value (e.g. "female"), [title] its localized label resolved from
 * `predefinedModelCategories`, [viewBlocks] the gender's models grouped by their `view`.
 *
 * Unlike [TryOnModelGenderUiModel] (which filters to the general view and flattens), the shoes flow
 * keeps every [ModelView] and renders one block per view.
 */
@Immutable
internal data class ShoesModelGenderUiModel(
    val id: String,
    val title: String,
    val viewBlocks: List<ShoesModelViewBlock>,
)

/**
 * A single `view` group inside a gender — rendered as a titled row of model thumbnails.
 *
 * [view] is the raw `view` tag value (e.g. "full-height", "bird-view") kept as a stable key,
 * [title] its display label resolved from the shoes-specific `predefinedModelShoesCategories`
 * (falling back to the raw [view] when the map has no entry), [models] the models with that view.
 */
@Immutable
internal data class ShoesModelViewBlock(
    val view: String,
    val title: String,
    val models: List<TryOnModelUiModel>,
)

/**
 * Builds the gender dimension for the SHOES model selector from a flat list of models.
 *
 * Builds one tab per [Gender.selectorGenders] value (folding [Gender.UNISEX] into both), then one
 * block per [ModelView] within it (in declaration order, empty views skipped). Gender titles are
 * resolved from the general [predefinedModelCategories] (genders without a matching label or
 * without models are skipped — no invented fallbacks), while view titles come from the
 * shoes-specific view categories dug out of [shoesMode], falling back to the raw `view` value.
 */
internal fun List<TryOnModelUiModel>.toShoesGenders(
    predefinedModelCategories: Map<String, String>,
    shoesMode: AiutaShoesMode?,
): List<ShoesModelGenderUiModel> {
    val predefinedModelShoesCategories = shoesMode
        ?.imagePicker
        ?.predefinedModels
        ?.strings
        ?.predefinedModelShoesCategories
        .orEmpty()

    return Gender.selectorGenders.mapNotNull { gender ->
        val title = predefinedModelCategories[gender.id] ?: return@mapNotNull null
        val genderModels = filter { model -> model.tags.gender.matches(gender) }
        if (genderModels.isEmpty()) return@mapNotNull null

        val viewBlocks = ModelView.entries.mapNotNull { view ->
            val models = genderModels.filter { model -> model.tags.view == view }
            if (models.isEmpty()) return@mapNotNull null
            ShoesModelViewBlock(
                view = view.id,
                title = predefinedModelShoesCategories[view.id] ?: view.id,
                models = models,
            )
        }

        ShoesModelGenderUiModel(
            id = gender.id,
            title = title,
            viewBlocks = viewBlocks,
        )
    }
}
