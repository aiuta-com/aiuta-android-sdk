package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaLogger
import com.aiuta.fashionsdk.logger.d
import com.aiuta.fashionsdk.logger.e
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaTryOnDataController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.data.providePowerBySticker
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.safeOpenUri

@Composable
internal fun AiutaLabel(modifier: Modifier = Modifier) {
    val theme = LocalTheme.current
    val dataController = LocalAiutaTryOnDataController.current
    val logger = LocalAiutaLogger.current
    val uriHandler = LocalUriHandler.current

    val isLabelVisible = remember { mutableStateOf(false) }
    val urlToOpen = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        dataController
            .providePowerBySticker()
            .onSuccess { feature ->
                isLabelVisible.value = feature?.isVisible ?: false
                urlToOpen.value = feature?.urlAndroid
                logger?.d("AiutaLabel: Power by sticker is visible: ${isLabelVisible.value}")
            }
            .onFailure { exception ->
                logger?.e("AiutaLabel: Failed to load power by sticker", exception)
            }
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = isLabelVisible.value,
    ) {
        Box(
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(100.dp),
                    color = theme.color.neutral,
                )
                .clickableUnindicated {
                    urlToOpen.value?.let { url ->
                        uriHandler.safeOpenUri(url)
                    }
                }
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = theme.powerBar.strings.poweredByAiuta,
                style = theme.button.typography.buttonS,
                color = theme.color.primary,
            )
        }
    }
}
