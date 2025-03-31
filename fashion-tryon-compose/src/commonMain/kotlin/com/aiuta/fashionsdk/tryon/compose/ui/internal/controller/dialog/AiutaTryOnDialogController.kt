package com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
internal fun rememberAiutaTryOnDialogController(): AiutaTryOnDialogController {
    val defaultState =
        remember {
            mutableStateOf<AiutaTryOnDialogState?>(null)
        }

    return remember {
        AiutaTryOnDialogController(
            state = defaultState,
        )
    }
}

@Immutable
internal class AiutaTryOnDialogController(
    val state: MutableState<AiutaTryOnDialogState?>,
)
