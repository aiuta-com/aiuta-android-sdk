package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.utils

import com.aiuta.fashionsdk.configuration.features.tryon.validation.strings.AiutaTryOnInputImageValidationFeatureStrings
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.exception.AiutaTryOnAbortReason

/**
 * Resolves the validation dialog description for the given abort [reason]. An unknown or
 * missing reason falls back to the generic `invalidInputImageDescription`.
 */
internal fun AiutaTryOnInputImageValidationFeatureStrings.descriptionFor(
    reason: AiutaTryOnAbortReason?,
): String = when (reason) {
    AiutaTryOnAbortReason.NO_PEOPLE_DETECTED -> noPeopleDetectedDescription
    AiutaTryOnAbortReason.TOO_MANY_PEOPLE_DETECTED -> tooManyPeopleDetectedDescription
    AiutaTryOnAbortReason.CHILD_DETECTED -> childDetectedDescription
    AiutaTryOnAbortReason.INSUFFICIENT_TARGET_AREA -> insufficientTargetAreaDescription
    AiutaTryOnAbortReason.INTERNAL_RESTRICTION -> internalRestrictionDescription
    AiutaTryOnAbortReason.UNKNOWN, null -> invalidInputImageDescription
}
