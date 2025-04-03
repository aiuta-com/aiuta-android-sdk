package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.components.footer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.components.AiutaLabel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.configuration.rememberScreenSize

@Composable
internal fun InactiveFooter(modifier: Modifier = Modifier) {
    val screenSize = rememberScreenSize()
    val horizontalPadding = screenSize.widthDp * 0.2f

    Column(
        modifier = modifier.padding(horizontal = horizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(1f))

        AiutaLabel()

        Spacer(Modifier.height(16.dp))
    }
}
