package com.aiuta.fashionsdk.internal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.internal.navigation.bottomsheet.AiutaNavigationBottomSheetScreen
import com.aiuta.fashionsdk.internal.navigation.bottomsheet.rememberAiutaBottomSheetNavigator
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaBottomSheetNavigator
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaDialogController
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaErrorSnackbarController
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaLogger
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.internal.navigation.controller.rememberAiutaNavigationController
import com.aiuta.fashionsdk.internal.navigation.dialog.rememberAiutaDialogController
import com.aiuta.fashionsdk.internal.navigation.snackbar.rememberAiutaErrorSnackbarController
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme

@Composable
public fun AiutaNavigationInitialisation(
    startScreen: AiutaNavigationScreen,
    aiutaConfiguration: AiutaConfiguration,
    bottomSheetEntryProvider: (AiutaNavigationBottomSheetScreen) -> AiutaNavEntry<AiutaNavigationBottomSheetScreen>,
    content: @Composable () -> Unit,
) {
    val theme = aiutaConfiguration.userInterface.theme
    val navigationController = rememberAiutaNavigationController(
        startScreen = startScreen,
        aiutaConfiguration = aiutaConfiguration,
    )
    val bottomSheetNavigator = rememberAiutaBottomSheetNavigator(
        bottomSheetEntryProvider = bottomSheetEntryProvider,
    )
    val errorSnackbarController = rememberAiutaErrorSnackbarController()
    val dialogController = rememberAiutaDialogController()

    CompositionLocalProvider(
        LocalAiutaNavigationController provides navigationController,
        LocalAiutaBottomSheetNavigator provides bottomSheetNavigator,
        LocalAiutaErrorSnackbarController provides errorSnackbarController,
        LocalAiutaDialogController provides dialogController,
        LocalTheme provides theme,
        LocalAiutaLogger provides aiutaConfiguration.aiuta.logger,
    ) {
        content()
    }
}
