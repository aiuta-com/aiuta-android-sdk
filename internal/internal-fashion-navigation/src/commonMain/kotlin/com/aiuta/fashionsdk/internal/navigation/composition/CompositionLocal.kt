package com.aiuta.fashionsdk.internal.navigation.composition

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.aiuta.fashionsdk.internal.navigation.bottomsheet.AiutaBottomSheetNavigator
import com.aiuta.fashionsdk.internal.navigation.controller.AiutaNavigationController
import com.aiuta.fashionsdk.internal.navigation.dialog.AiutaDialogController
import com.aiuta.fashionsdk.internal.navigation.snackbar.AiutaErrorSnackbarController

public val LocalNavigationController: ProvidableCompositionLocal<AiutaNavigationController> =
    staticCompositionLocalOf {
        noLocalProvidedFor("LocalNavigationController")
    }

public val LocalBottomSheetNavigator: ProvidableCompositionLocal<AiutaBottomSheetNavigator> =
    staticCompositionLocalOf {
        noLocalProvidedFor("LocalBottomSheetNavigator")
    }

public val LocalErrorSnackbarController: ProvidableCompositionLocal<AiutaErrorSnackbarController> =
    staticCompositionLocalOf {
        noLocalProvidedFor("LocalErrorSnackbarController")
    }

public val LocalAiutaDialogController: ProvidableCompositionLocal<AiutaDialogController> =
    staticCompositionLocalOf {
        noLocalProvidedFor("LocalAiutaDialogController")
    }

private fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}
