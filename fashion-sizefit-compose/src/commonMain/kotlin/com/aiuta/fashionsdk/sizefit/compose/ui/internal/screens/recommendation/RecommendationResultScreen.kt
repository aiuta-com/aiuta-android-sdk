package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.recommendation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiuta.fashionsdk.compose.uikit.appbar.AiutaAppBar
import com.aiuta.fashionsdk.compose.uikit.appbar.AiutaAppBarIcon
import com.aiuta.fashionsdk.compose.uikit.button.FashionButton
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonSizes
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonStyles
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.sizefit.AiutaSizeFitFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.navigation.SizeFitScreen
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.SizeFitConfigState
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.recommendation.components.SizeRecommendationProgress
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFitConfig

@Composable
internal fun RecommendationResultScreen(
    args: SizeFitScreen.RecommendationResult,
    modifier: Modifier = Modifier,
) {
    val navigationController = LocalAiutaNavigationController.current
    val theme = LocalTheme.current

    val sizeFitFeature = strictProvideFeature<AiutaSizeFitFeature>()
    val sizeFitConfig = args.config
    val sizeFitRecommendation = args.recommendation
    val sharedProgressModifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp)

    val viewModel = viewModel { RecommendationResultViewModel() }
    val (recommendedSizeInfo, nextBestSizeInfo) = remember(sizeFitRecommendation) {
        viewModel.calculateRecommendationData(sizeFitRecommendation)
    }

    Column(
        modifier = modifier
            .background(theme.color.background)
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AiutaAppBar(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            actions = {
                AiutaAppBarIcon(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    icon = theme.pageBar.icons.close24,
                    color = theme.color.primary,
                    onClick = navigationController::clickClose,
                )
            },
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = sizeFitFeature.strings.recommendedSizeTitle,
            style = theme.label.typography.titleM,
            color = theme.color.primary,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = sizeFitRecommendation.recommendedSizeName,
            style = theme.label.typography.titleM.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 140.sp,
            ),
            color = theme.color.primary,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.weight(1f))

        // Recommended size
        recommendedSizeInfo?.let { sizeInfo ->
            SizeRecommendationProgress(
                sizeName = sizeInfo.size.name,
                percentage = sizeInfo.confidence,
                progressColor = Brush.horizontalGradient(sizeFitFeature.styles.sizeFitButtonGradient),
                percentageTextColor = theme.color.background,
                fitSummary = sizeInfo.fitSummary,
                modifier = sharedProgressModifier,
            )
        }

        Spacer(Modifier.height(26.dp))

        // Next best size
        nextBestSizeInfo?.let { sizeInfo ->
            SizeRecommendationProgress(
                sizeName = sizeInfo.size.name,
                percentage = sizeInfo.confidence,
                fitSummary = sizeInfo.fitSummary,
                modifier = sharedProgressModifier,
            )
        }

        Spacer(Modifier.weight(1f))

        ParametersBlock(
            sizeFitConfig = sizeFitConfig,
        )

        Spacer(Modifier.height(36.dp))

        FashionButton(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            text = sizeFitFeature.strings.gotItButton,
            style = FashionButtonStyles.primaryStyle(theme),
            size = FashionButtonSizes.lSize(),
            onClick = navigationController::clickClose,
        )

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun ColumnScope.ParametersBlock(
    sizeFitConfig: SizeFitConfigState,
    modifier: Modifier = Modifier,
) {
    val navigationController = LocalAiutaNavigationController.current
    val theme = LocalTheme.current

    val sizeFitFeature = strictProvideFeature<AiutaSizeFitFeature>()

    Text(
        text = sizeFitFeature.strings.parametersTitle,
        style = theme.label.typography.subtle,
        color = theme.color.primary,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(8.dp))

    Text(
        text = listOfNotNull(
            when (sizeFitConfig.gender) {
                AiutaSizeFitConfig.Gender.FEMALE -> sizeFitFeature.strings.genderFemale
                AiutaSizeFitConfig.Gender.MALE -> sizeFitFeature.strings.genderMale
            }.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
            "${sizeFitConfig.age} ${sizeFitFeature.strings.yearsSuffix}",
            "${sizeFitConfig.height} ${sizeFitFeature.strings.heightSuffix.lowercase()}",
            "${sizeFitConfig.weight} ${sizeFitFeature.strings.weightSuffix.lowercase()}",
        ).joinToString(" / "),
        style = theme.label.typography.subtle,
        color = theme.color.primary,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(16.dp))

    Text(
        text = sizeFitFeature.strings.changeButton,
        style = theme.button.typography.buttonM,
        color = theme.color.secondary,
        textAlign = TextAlign.Center,
        modifier = Modifier.clickableUnindicated(
            onClick = {
                navigationController.navigateTo(SizeFitScreen.Questionary)
            },
        ),
    )
}
