package com.aiuta.fashionsdk.internal.navigation.dialog

import androidx.compose.runtime.Immutable

@Immutable
public class AiutaDialogState(
    val title: String? = null,
    val description: String,
    val confirmButton: String,
    val onConfirm: () -> Unit,
    val onDismiss: (() -> Unit)? = null,
)
