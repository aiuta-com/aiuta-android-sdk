package com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.internal.analytic.internalAiutaAnalytic
import com.aiuta.fashionsdk.tryon.compose.domain.internal.language.resolveInternalLanguage
import com.aiuta.fashionsdk.tryon.compose.domain.models.AiutaTryOnConfiguration
import com.aiuta.fashionsdk.tryon.compose.domain.models.AiutaTryOnListeners
import com.aiuta.fashionsdk.tryon.compose.domain.models.AiutaTryOnTheme
import com.aiuta.fashionsdk.tryon.compose.domain.models.SKUItem
import com.aiuta.fashionsdk.tryon.compose.domain.models.defaultAiutaTryOnConfiguration
import com.aiuta.fashionsdk.tryon.compose.domain.models.toTheme
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.LocalAiutaConfiguration
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.LocalAiutaTryOnStringResources
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.LocalAnalytic
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.rememberFashionTryOnController
import com.aiuta.fashionsdk.tryon.core.AiutaTryOn

@Composable
internal fun NavigationInitialisation(
    modifier: Modifier = Modifier,
    aiuta: () -> Aiuta,
    aiutaTryOn: () -> AiutaTryOn,
    aiutaTryOnListeners: () -> AiutaTryOnListeners,
    aiutaTryOnConfiguration: (() -> AiutaTryOnConfiguration)?,
    theme: (() -> AiutaTryOnTheme)?,
    skuForGeneration: () -> SKUItem,
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier,
    ) {
        val internalAnalytic = remember { aiuta().internalAiutaAnalytic }
        val internalTheme = remember { theme?.invoke().toTheme() }
        val configuration =
            remember {
                aiutaTryOnConfiguration?.invoke() ?: defaultAiutaTryOnConfiguration()
            }
        val controller =
            rememberFashionTryOnController(
                analytic = { internalAnalytic },
                aiuta = aiuta,
                aiutaTryOn = aiutaTryOn,
                aiutaTryOnListeners = aiutaTryOnListeners,
                aiutaTryOnConfiguration = configuration,
                skuForGeneration = skuForGeneration,
            )

        CompositionLocalProvider(
            LocalAnalytic provides internalAnalytic,
            LocalController provides controller,
            LocalTheme provides internalTheme,
            LocalAiutaConfiguration provides configuration,
            LocalAiutaTryOnStringResources provides
                resolveInternalLanguage(
                    selectedLanguage = configuration.language,
                ),
        ) {
            content()
        }
    }
}
