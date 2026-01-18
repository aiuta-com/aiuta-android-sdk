package com.aiuta.fashionsdk.internal.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.internal.navigation.bottomsheet.AiutaNavigationBottomSheetScreen
import com.aiuta.fashionsdk.internal.navigation.bottomsheet.rememberAiutaBottomSheetNavigator
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaDialogController
import com.aiuta.fashionsdk.internal.navigation.composition.LocalBottomSheetNavigator
import com.aiuta.fashionsdk.internal.navigation.composition.LocalErrorSnackbarController
import com.aiuta.fashionsdk.internal.navigation.composition.LocalNavigationController
import com.aiuta.fashionsdk.internal.navigation.controller.rememberAiutaNavigationController
import com.aiuta.fashionsdk.internal.navigation.dialog.isDialogVisible
import com.aiuta.fashionsdk.internal.navigation.dialog.rememberAiutaDialogController
import com.aiuta.fashionsdk.internal.navigation.internal.components.DEFAULT_SHOWING_DELAY
import com.aiuta.fashionsdk.internal.navigation.internal.components.NavigationAlertDialog
import com.aiuta.fashionsdk.internal.navigation.internal.components.NavigationContent
import com.aiuta.fashionsdk.internal.navigation.internal.components.NavigationErrorCard
import com.aiuta.fashionsdk.internal.navigation.snackbar.isErrorStateVisible
import com.aiuta.fashionsdk.internal.navigation.snackbar.rememberAiutaErrorSnackbarController
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme
import kotlinx.coroutines.delay

@Composable
public fun AiutaNavigationFlow(
    startScreen: AiutaNavigationScreen,
    aiutaConfiguration: AiutaConfiguration,
    clickClose: () -> Unit,
    bottomSheetEntryProvider: (AiutaNavigationBottomSheetScreen) -> AiutaNavEntry<AiutaNavigationBottomSheetScreen>,
    contentEntryProvider: (AiutaNavigationScreen) -> AiutaNavEntry<AiutaNavigationScreen>,
    modifier: Modifier = Modifier,
) {
    val theme = aiutaConfiguration.userInterface.theme
    val navigationController = rememberAiutaNavigationController(
        startScreen = startScreen,
        clickClose = clickClose,
    )
    val bottomSheetNavigator = rememberAiutaBottomSheetNavigator(
        bottomSheetEntryProvider = bottomSheetEntryProvider,
    )
    val errorSnackbarController = rememberAiutaErrorSnackbarController()
    val dialogController = rememberAiutaDialogController()

    CompositionLocalProvider(
        LocalNavigationController provides navigationController,
        LocalBottomSheetNavigator provides bottomSheetNavigator,
        LocalErrorSnackbarController provides errorSnackbarController,
        LocalAiutaDialogController provides dialogController,
        LocalTheme provides theme,
    ) {
        Box(modifier = modifier) {
            ModalBottomSheetLayout(
                modifier = Modifier.fillMaxSize(),
                sheetState = bottomSheetNavigator.sheetState,
                sheetBackgroundColor = theme.color.background,
                sheetContent = bottomSheetNavigator.sheetContent,
                sheetContentColor = theme.color.primary,
                sheetShape = theme.bottomSheet.shapes.bottomSheetShape,
                sheetElevation = 0.dp,
                content = {
                    NavigationContent(
                        modifier = Modifier.fillMaxSize(),
                        contentEntryProvider = contentEntryProvider,
                    )
                },
            )

            AnimatedVisibility(
                modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                visible = errorSnackbarController.isErrorStateVisible().value,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                errorSnackbarController.errorState.value?.let { state ->

                    LaunchedEffect(Unit) {
                        delay(DEFAULT_SHOWING_DELAY)

                        errorSnackbarController.hideErrorState().also {
                            state.onClose?.invoke()
                        }
                    }

                    NavigationErrorCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        errorState = state,
                    )
                }
            }

            AnimatedVisibility(
                modifier = Modifier.fillMaxSize(),
                visible = dialogController.isDialogVisible().value,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                dialogController.state.value?.let { state ->
                    NavigationAlertDialog(state = state)
                }
            }
        }
    }
}
