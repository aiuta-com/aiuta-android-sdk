package com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.components.appbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated

@Composable
internal fun AppBar(
    modifier: Modifier = Modifier,
    navigationIcon: @Composable BoxScope.() -> Unit = {},
    title: @Composable BoxScope.() -> Unit = {},
    actions: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        modifier =
        modifier
            .padding(vertical = 14.dp)
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        navigationIcon()

        title()

        actions()
    }
}

@Composable
internal fun AppBarIcon(
    modifier: Modifier = Modifier,
    icon: AiutaIcon,
    color: Color,
    onClick: () -> Unit,
) {
    AiutaIcon(
        modifier = modifier
            .size(24.dp)
            .clickableUnindicated { onClick() },
        icon = icon,
        tint = color,
        contentDescription = null,
    )
}
