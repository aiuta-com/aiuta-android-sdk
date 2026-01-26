package com.aiuta.fashionsdk.sizefit.compose.ui.internal.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.internal.navigation.AiutaNavigationFlow
import com.aiuta.fashionsdk.internal.navigation.aiutaEntryProvider
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.QuestionaryScreen
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.recommendation.RecommendationResultScreen
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFit

@Composable
internal fun SizeFitNavigationFlow(
    aiutaConfiguration: AiutaConfiguration,
    aiutaSizeFit: AiutaSizeFit,
    productCode: String,
    modifier: Modifier = Modifier,
) {
    val sharedModifier = Modifier.fillMaxSize()

    SizeFitNavigationInitialisation(
        aiutaSizeFit = aiutaSizeFit,
        aiutaConfiguration = aiutaConfiguration,
    ) {
        AiutaNavigationFlow(
            contentEntryProvider = aiutaEntryProvider {
                aiutaEntry<SizeFitScreen.Questionary> {
                    QuestionaryScreen(
                        productCode = productCode,
                        modifier = sharedModifier,
                    )
                }
                aiutaEntry<SizeFitScreen.RecommendationResult> { args ->
                    RecommendationResultScreen(
                        args = args,
                        modifier = sharedModifier,
                    )
                }
            },
            modifier = modifier,
        )
    }
}
