package com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel

/**
 * Recognized `view` tag values of a try-on model.
 *
 * [id] is the raw backend value, also used as the key to resolve a localized title from the
 * shoes category map. Unrecognized values are dropped before reaching the UI (see
 * [TryOnModelUiModel] mapping), so there is no `UNKNOWN` entry here.
 *
 * IMPORTANT: the **declaration order is the display order** — `toShoesGenders` builds the view
 * blocks by iterating [entries], so this list is what dictates that full-height appears first,
 * then bird-view, then side-view. Reordering these entries reorders the blocks on screen.
 */
internal enum class ModelView(val id: String) {
    FULL_HEIGHT("full-height"),
    BIRD_VIEW("bird-view"),
    SIDE_VIEW("side-view"),
    ;

    internal companion object {
        fun from(id: String?): ModelView? = entries.find { it.id == id }
    }
}
