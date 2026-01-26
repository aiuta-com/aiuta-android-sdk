package com.aiuta.fashionsdk.sizefit.compose.ui.internal.composition

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFit

internal val LocalAiutaSizeFit: ProvidableCompositionLocal<AiutaSizeFit> =
    staticCompositionLocalOf {
        noLocalProvidedFor("LocalAiutaSizeFit")
    }

private fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}
