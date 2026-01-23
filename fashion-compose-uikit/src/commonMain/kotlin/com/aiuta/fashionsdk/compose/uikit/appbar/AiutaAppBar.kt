package com.aiuta.fashionsdk.compose.uikit.appbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated

@Composable
public fun AiutaAppBar(
    modifier: Modifier = Modifier,
    navigationIcon: @Composable BoxScope.(modifier: Modifier) -> Unit,
    title: @Composable BoxScope.(modifier: Modifier) -> Unit,
    closeButton: @Composable BoxScope.(modifier: Modifier) -> Unit,
) {
    val theme = LocalTheme.current

    AiutaAppBar(
        modifier = modifier,
        navigationIcon = {
            val navigationIconModifier = Modifier.align(Alignment.CenterStart)

            if (theme.pageBar.toggles.preferCloseButtonOnTheRight) {
                navigationIcon(navigationIconModifier)
            } else {
                closeButton(navigationIconModifier)
            }
        },
        title = {
            val titleModifier = Modifier.align(Alignment.Center)

            title(titleModifier)
        },
        actions = {
            val actionModifier = Modifier.align(Alignment.CenterEnd)

            if (theme.pageBar.toggles.preferCloseButtonOnTheRight) {
                closeButton(actionModifier)
            } else {
                navigationIcon(actionModifier)
            }
        },
    )
}

@Composable
public fun AiutaAppBar(
    modifier: Modifier = Modifier,
    navigationIcon: @Composable BoxScope.() -> Unit = {},
    title: @Composable BoxScope.() -> Unit = {},
    actions: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        modifier = modifier
            .padding(vertical = 14.dp)
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        navigationIcon()

        title()

        actions()
    }
}

@Composable
public fun AiutaAppBarIcon(
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
