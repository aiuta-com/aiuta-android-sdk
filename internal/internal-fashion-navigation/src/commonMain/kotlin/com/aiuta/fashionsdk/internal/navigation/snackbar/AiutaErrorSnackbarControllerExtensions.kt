package com.aiuta.fashionsdk.internal.navigation.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember

@Composable
internal fun AiutaErrorSnackbarController.isErrorStateVisible(): State<Boolean> = remember {
    derivedStateOf {
        errorState.value != null
    }
}
