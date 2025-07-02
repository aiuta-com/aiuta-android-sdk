package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.list.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.icons.AiutaLoadingComponent
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.uikit.utils.clickableUnindicated

@Composable
internal fun ShareButton(
    modifier: Modifier = Modifier,
    isLoading: State<Boolean>,
    shareText: String,
    onClick: () -> Unit,
) {
    val theme = LocalTheme.current

    Box(
        modifier = modifier
            .background(
                color = theme.color.onDark,
                shape = RoundedCornerShape(18.dp),
            )
            .clickableUnindicated(onClick = onClick)
            .padding(
                horizontal = 16.dp,
                vertical = 10.dp,
            ),
        contentAlignment = Alignment.Center,
    ) {
        AiutaLoadingComponent(
            modifier = Modifier.size(24.dp),
            circleColor = theme.color.onLight,
            isLoading = isLoading.value,
            component = {
                Text(
                    style = theme.button.typography.buttonM,
                    color = theme.color.onLight,
                    textAlign = TextAlign.Center,
                    text = shareText,
                )
            },
        )
    }
}
