package com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel

import androidx.compose.runtime.Immutable

internal const val TAG_GENDER = "gender"
internal const val TAG_VIEW = "view"

/**
 * Typed projection of a try-on model's raw `tags` map — only the tags the UI actually consumes.
 */
@Immutable
internal data class TryOnModelTags(
    val gender: Gender,
    val view: ModelView,
)

/**
 * Parses the raw tag map into [TryOnModelTags]. Returns `null` when `gender` or `view` is missing
 * or holds an unrecognized value — such models are simply not shown (see [Gender] / [ModelView]).
 */
internal fun Map<String, String>.toTryOnModelTags(): TryOnModelTags? {
    val gender = Gender.from(this[TAG_GENDER]) ?: return null
    val view = ModelView.from(this[TAG_VIEW]) ?: return null
    return TryOnModelTags(
        gender = gender,
        view = view,
    )
}
