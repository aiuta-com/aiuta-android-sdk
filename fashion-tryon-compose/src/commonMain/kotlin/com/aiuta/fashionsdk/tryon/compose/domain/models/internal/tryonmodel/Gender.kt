package com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel

/**
 * Recognized `gender` tag values of a try-on model.
 *
 * [id] is the raw backend value, also used as the key to resolve a localized title from the
 * customer-configured category maps. Unrecognized values are dropped before reaching the UI (see
 * [TryOnModelUiModel] mapping), so there is no `UNKNOWN` entry here.
 */
internal enum class Gender(val id: String) {
    MALE("male"),
    FEMALE("female"),
    UNISEX("unisex"),
    ;

    /**
     * Whether a model with this gender should appear under the [selector] tab. [UNISEX] models
     * appear under every tab.
     */
    fun matches(selector: Gender): Boolean = this == selector || this == UNISEX

    internal companion object {
        fun from(id: String?): Gender? = entries.find { it.id == id }

        /**
         * Genders shown as tabs in the selector. [UNISEX] is never its own tab — its models are
         * folded into both [FEMALE] and [MALE] instead.
         */
        val selectorGenders: List<Gender> = listOf(FEMALE, MALE)
    }
}
