package com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.aiuta.fashionsdk.internal.analytics.InternalAiutaAnalytic
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.data.AiutaTryOnDataController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.loading.AiutaTryOnLoadingActionsController

// Analytic
internal val LocalAnalytic: ProvidableCompositionLocal<InternalAiutaAnalytic> =
    staticCompositionLocalOf {
        noLocalProvidedFor("LocalAnalytic")
    }

// Controllers
// Data
internal val LocalAiutaTryOnDataController: ProvidableCompositionLocal<AiutaTryOnDataController> =
    staticCompositionLocalOf {
        noLocalProvidedFor("AiutaTryOnDataController")
    }

internal val LocalAiutaTryOnLoadingActionsController: ProvidableCompositionLocal<AiutaTryOnLoadingActionsController> =
    staticCompositionLocalOf {
        noLocalProvidedFor("AiutaTryOnLoadingActionsController")
    }

// Base controller
internal val LocalController: ProvidableCompositionLocal<FashionTryOnController> =
    staticCompositionLocalOf {
        noLocalProvidedFor("LocalController")
    }

private fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}
