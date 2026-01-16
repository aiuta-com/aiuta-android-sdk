package com.aiuta.fashionsdk.internal.navigation.snackbar

import androidx.compose.runtime.Immutable

@Immutable
public interface AiutaErrorSnackbarState {
    val message: String?
    val onRetry: () -> Unit
    val onClose: (() -> Unit)?
}
