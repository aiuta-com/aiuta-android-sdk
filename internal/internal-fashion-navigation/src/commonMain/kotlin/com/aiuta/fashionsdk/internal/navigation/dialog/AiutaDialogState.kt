package com.aiuta.fashionsdk.internal.navigation.dialog

import androidx.compose.runtime.Immutable

@Immutable
public class AiutaDialogState(
    public val title: String? = null,
    public val description: String,
    public val confirmButton: String,
    public val onConfirm: () -> Unit,
    public val onDismiss: (() -> Unit)? = null,
)
