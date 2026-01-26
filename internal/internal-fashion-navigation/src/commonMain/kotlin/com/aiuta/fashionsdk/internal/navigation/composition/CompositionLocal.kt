package com.aiuta.fashionsdk.internal.navigation.composition

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.aiuta.fashionsdk.internal.navigation.bottomsheet.AiutaBottomSheetNavigator
import com.aiuta.fashionsdk.internal.navigation.controller.AiutaNavigationController
import com.aiuta.fashionsdk.internal.navigation.dialog.AiutaDialogController
import com.aiuta.fashionsdk.internal.navigation.snackbar.AiutaErrorSnackbarController
import com.aiuta.fashionsdk.logger.AiutaLogger

// Navigation
public val LocalAiutaNavigationController: ProvidableCompositionLocal<AiutaNavigationController> =
    staticCompositionLocalOf {
        noLocalProvidedFor("LocalAiutaNavigationController")
    }

public val LocalAiutaBottomSheetNavigator: ProvidableCompositionLocal<AiutaBottomSheetNavigator> =
    staticCompositionLocalOf {
        noLocalProvidedFor("LocalAiutaBottomSheetNavigator")
    }

public val LocalAiutaErrorSnackbarController: ProvidableCompositionLocal<AiutaErrorSnackbarController> =
    staticCompositionLocalOf {
        noLocalProvidedFor("LocalAiutaErrorSnackbarController")
    }

public val LocalAiutaDialogController: ProvidableCompositionLocal<AiutaDialogController> =
    staticCompositionLocalOf {
        noLocalProvidedFor("LocalAiutaDialogController")
    }

// Logger
public val LocalAiutaLogger: ProvidableCompositionLocal<AiutaLogger?> =
    staticCompositionLocalOf {
        noLocalProvidedFor("LocalAiutaLogger")
    }

private fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}
