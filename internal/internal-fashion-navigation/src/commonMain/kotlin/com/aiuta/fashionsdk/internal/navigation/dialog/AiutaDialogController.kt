package com.aiuta.fashionsdk.internal.navigation.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
internal fun rememberAiutaDialogController(): AiutaDialogController = remember { AiutaDialogController() }

@Immutable
public class AiutaDialogController internal constructor() {
    internal val state: MutableState<AiutaDialogState?> = mutableStateOf(null)

    public fun showDialog(dialogState: AiutaDialogState) {
        state.value = dialogState
    }

    public fun hideDialog() {
        state.value = null
    }
}
