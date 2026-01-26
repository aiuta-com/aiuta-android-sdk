package com.aiuta.fashionsdk.internal.navigation.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
internal fun rememberAiutaErrorSnackbarController(): AiutaErrorSnackbarController = remember { AiutaErrorSnackbarController() }

@Immutable
public class AiutaErrorSnackbarController internal constructor() {
    internal val errorState: MutableState<AiutaErrorSnackbarState?> = mutableStateOf(null)

    public fun showErrorState(newErrorState: AiutaErrorSnackbarState) {
        // Check if toast already visible
        if (errorState.value == null) {
            errorState.value = newErrorState
        }
    }

    public fun hideErrorState() {
        errorState.value = null
    }
}
