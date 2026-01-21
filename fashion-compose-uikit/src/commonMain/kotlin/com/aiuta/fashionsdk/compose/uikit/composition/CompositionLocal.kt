package com.aiuta.fashionsdk.compose.uikit.composition

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.aiuta.fashionsdk.configuration.ui.theme.AiutaTheme

/**
 * A composition local that provides access to the current [AiutaTheme].
 * This can be used to access theme values throughout the composition tree.
 */
public val LocalTheme: ProvidableCompositionLocal<AiutaTheme> =
    staticCompositionLocalOf {
        noLocalProvidedFor("LocalTheme")
    }

/**
 * Throws an error when a required composition local is not provided.
 *
 * @param name The name of the missing composition local
 * @throws IllegalStateException with a descriptive error message
 */
private fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}
