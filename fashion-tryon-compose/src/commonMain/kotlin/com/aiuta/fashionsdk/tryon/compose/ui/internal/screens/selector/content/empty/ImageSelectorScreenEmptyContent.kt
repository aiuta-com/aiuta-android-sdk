package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.content.empty

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.components.AiutaLabel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.content.empty.body.ImageSelectorScreenEmptyBodyBlock

@Composable
internal fun ImageSelectorScreenEmptyContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(40.dp))

        ImageSelectorScreenEmptyBodyBlock(
            modifier = Modifier.fillMaxWidth().weight(1f),
        )

        Spacer(Modifier.height(60.dp))

        AiutaLabel()

        Spacer(Modifier.height(16.dp))
    }
}
