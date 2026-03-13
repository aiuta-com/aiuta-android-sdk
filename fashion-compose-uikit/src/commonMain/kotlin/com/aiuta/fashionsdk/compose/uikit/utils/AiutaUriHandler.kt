package com.aiuta.fashionsdk.compose.uikit.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.UriHandler
import com.aiuta.fashionsdk.logger.AiutaLogger

@Composable
internal expect fun rememberAiutaUriHandler(logger: AiutaLogger?): UriHandler
