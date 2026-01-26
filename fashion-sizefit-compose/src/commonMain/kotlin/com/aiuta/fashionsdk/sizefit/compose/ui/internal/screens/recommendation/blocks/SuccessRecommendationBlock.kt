package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.recommendation.blocks

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.sizefit.AiutaSizeFitFeature
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.navigation.SizeFitScreen
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.recommendation.RecommendationResultViewModel
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.recommendation.components.SizeRecommendationProgress

@Composable
internal fun ColumnScope.SuccessRecommendationBlock(
    args: SizeFitScreen.RecommendationResult,
) {
    val theme = LocalTheme.current

    val sizeFitFeature = strictProvideFeature<AiutaSizeFitFeature>()
    val sizeFitRecommendation = args.recommendation

    val sharedProgressModifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp)

    val viewModel = viewModel { RecommendationResultViewModel() }
    val (recommendedSizeInfo, nextBestSizeInfo) = remember(sizeFitRecommendation) {
        viewModel.calculateRecommendationData(sizeFitRecommendation)
    }

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
}
