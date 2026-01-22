package com.aiuta.fashionsdk.internal.navigation.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember

@Composable
internal fun AiutaDialogController.isDialogVisible(): State<Boolean> = remember {
    derivedStateOf {
        state.value != null
    }
}
