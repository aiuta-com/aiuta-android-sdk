package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.recommendation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme

@Composable
internal fun SizeRecommendationProgress(
    sizeName: String,
    percentage: Int,
    modifier: Modifier = Modifier,
    progressColor: Brush? = null,
    percentageTextColor: Color? = null,
    fitSummary: String? = null,
) {
    val theme = LocalTheme.current

    val actualProgressColor = progressColor ?: Brush.horizontalGradient(
        colors = listOf(
            theme.color.neutral,
            theme.color.neutral,
        ),
    )
    val actualPercentageTextColor = percentageTextColor ?: theme.color.primary

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        TopRecommendationBlock(
            sizeName = sizeName,
            percentage = percentage,
            progressColor = actualProgressColor,
            percentageTextColor = actualPercentageTextColor,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(6.dp))

        BottomRecommendationBlock(
            sizeName = sizeName,
            percentage = percentage,
            progressColor = actualProgressColor,
            modifier = Modifier.fillMaxWidth(),
        )

        fitSummary?.let {
            Spacer(Modifier.height(14.dp))

            Text(
                text = fitSummary,
                style = theme.productBar.typography.product,
                color = theme.color.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }
    }
}
