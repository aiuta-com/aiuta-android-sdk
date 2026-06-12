package com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.mode

import androidx.compose.runtime.Composable
import com.aiuta.fashionsdk.compose.uikit.composition.LocalAiutaConfiguration
import com.aiuta.fashionsdk.configuration.debug.AiutaValidationPolicy
import com.aiuta.fashionsdk.configuration.mode.AiutaMode
import com.aiuta.fashionsdk.logger.AiutaLogger
import com.aiuta.fashionsdk.logger.w
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaMode

internal fun <T> T?.orModeFallback(
    propertyName: String,
    fallback: () -> T,
    policy: AiutaValidationPolicy,
    logger: AiutaLogger?,
): T {
    if (this != null) return this
    val message = "MODE FALLBACK: $propertyName is not configured for the active mode, falling back to GENERAL"
    when (policy) {
        AiutaValidationPolicy.IGNORE -> Unit
        AiutaValidationPolicy.WARNING -> logger?.w(message)
        AiutaValidationPolicy.FATAL -> error(message)
    }
    return fallback()
}

@Composable
internal fun <T> resolveByMode(
    propertyName: String,
    general: () -> T,
    shoes: () -> T?,
): T {
    val mode = LocalAiutaMode.current
    val configuration = LocalAiutaConfiguration.current

    return when (mode) {
        AiutaMode.GENERAL -> general()
        AiutaMode.SHOES -> shoes().orModeFallback(
            propertyName = propertyName,
            fallback = general,
            policy = configuration.debugSettings.modeFallbackPolicy,
            logger = configuration.aiuta.logger,
        )
    }
}
