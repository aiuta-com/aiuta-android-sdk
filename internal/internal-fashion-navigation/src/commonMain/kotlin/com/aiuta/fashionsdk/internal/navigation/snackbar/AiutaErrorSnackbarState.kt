package com.aiuta.fashionsdk.internal.navigation.snackbar

import androidx.compose.runtime.Immutable

@Immutable
public interface AiutaErrorSnackbarState {
    public val message: String?
    public val onRetry: () -> Unit
    public val onClose: (() -> Unit)?
}
