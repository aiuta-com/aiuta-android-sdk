package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.recommendation

import androidx.lifecycle.ViewModel
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFitRecommendation
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.roundToInt

internal class RecommendationResultViewModel : ViewModel() {

    /**
     * Configuration for confidence calculation.
     */
    data class ConfidenceConfig(
        /** Falloff stiffness (0.6...1.2 is usually fine) */
        val k: Double = 0.8,
        /** Penalize tight (fitRatio < 0) stronger than loose */
        val tightPenaltyMultiplier: Double = 1.6,
        /** Measurement weights (can be adjusted per code/category) */
        val measurementWeights: Map<AiutaSizeFitRecommendation.Size.Measurement.MeasurementType, Double> = mapOf(
            AiutaSizeFitRecommendation.Size.Measurement.MeasurementType.WAIST_C to 2.0,
            AiutaSizeFitRecommendation.Size.Measurement.MeasurementType.HIP_C to 1.2,
            AiutaSizeFitRecommendation.Size.Measurement.MeasurementType.CHEST_C to 2.0,
            AiutaSizeFitRecommendation.Size.Measurement.MeasurementType.OVER_BUST to 1.0,
            AiutaSizeFitRecommendation.Size.Measurement.MeasurementType.UNDER_BUST to 1.5,
            AiutaSizeFitRecommendation.Size.Measurement.MeasurementType.BRA_UNDERBUST to 1.5,
        ),
        val defaultWeight: Double = 1.0,
    )

    enum class FitDirection {
        TIGHT,
        LOOSE,
    }

    enum class FitTone {
        SLIGHT,
        MAY_FEEL,
        EXACT,
    }

    data class FitGroupKey(
        val direction: FitDirection,
        val tone: FitTone,
    )

    data class SizeInfo(
        val size: AiutaSizeFitRecommendation.Size,
        val confidence: Int,
        val fitSummary: String?,
    )

    /**
     * Calculate data for the recommendation screen.
     */
    fun calculateRecommendationData(
        recommendation: AiutaSizeFitRecommendation,
        config: ConfidenceConfig = ConfidenceConfig(),
    ): Pair<SizeInfo?, SizeInfo?> {
        val recommendedSize = recommendation.recommendedSize
        val nextBestSize = recommendation.nextBestSize(config)
        val defaultConfidence = 25

        val recommendedSizeInfo = recommendedSize?.let { size ->
            SizeInfo(
                size = size,
                confidence = recommendation
                    .absoluteConfidencePercent(size, config)
                    .coerceAtLeast(defaultConfidence),
                fitSummary = size.measurements.fitSummary(isRecommended = true),
            )
        }

        val nextBestSizeInfo = nextBestSize?.let { size ->
            SizeInfo(
                size = size,
                confidence = recommendation
                    .absoluteConfidencePercent(size, config)
                    .coerceAtLeast(defaultConfidence - 5),
                fitSummary = size.measurements.fitSummary(isRecommended = false),
            )
        }

        return recommendedSizeInfo to nextBestSizeInfo
    }

    // Extension functions for AiutaSizeFitRecommendation

    private val AiutaSizeFitRecommendation.recommendedSize: AiutaSizeFitRecommendation.Size?
        get() = sizes.firstOrNull { it.name == recommendedSizeName }

    /**
     * For this API: fitRatio < 0 => tight, fitRatio > 0 => loose
     */
    private fun isTight(fitRatio: Float): Boolean = fitRatio < 0

    /**
     * Absolute confidence 0...1 (NOT normalized, depends only on the fitRatio grid).
     */
    private fun AiutaSizeFitRecommendation.absoluteConfidence(
        size: AiutaSizeFitRecommendation.Size,
        config: ConfidenceConfig,
    ): Double {
        if (size.measurements.isEmpty()) return 0.0

        var sumWeights = 0.0
        var sumWeightedLog = 0.0

        for (m in size.measurements) {
            val w = config.measurementWeights[m.type] ?: config.defaultWeight
            val dirMult = if (isTight(m.fitRatio)) config.tightPenaltyMultiplier else 1.0

            // soft per-measurement score
            val score = exp(-config.k * abs(m.fitRatio) * dirMult)

            // geometric mean via logarithms
            sumWeights += w
            sumWeightedLog += w * ln(score.coerceAtLeast(1e-12)) // guard against log(0)
        }

        return if (sumWeights > 0) exp(sumWeightedLog / sumWeights) else 0.0
    }

    private fun AiutaSizeFitRecommendation.absoluteConfidencePercent(
        size: AiutaSizeFitRecommendation.Size,
        config: ConfidenceConfig,
    ): Int = (absoluteConfidence(size, config) * 100).roundToInt()

    /**
     * Ranking by absolute confidence (to choose the "next best" by quality).
     */
    private fun AiutaSizeFitRecommendation.rankedByAbsoluteConfidence(
        config: ConfidenceConfig,
    ): List<Pair<AiutaSizeFitRecommendation.Size, Double>> = sizes
        .map { it to absoluteConfidence(it, config) }
        .sortedByDescending { it.second }

    /**
     * The "next" option: second-best by confidence (not necessarily adjacent size)
     */
    private fun AiutaSizeFitRecommendation.nextBestSize(
        config: ConfidenceConfig,
    ): AiutaSizeFitRecommendation.Size? {
        val ranked = rankedByAbsoluteConfidence(config)
        val recIdx = ranked.indexOfFirst { it.first.name == recommendedSizeName }

        if (recIdx == -1) {
            return ranked.drop(1).firstOrNull()?.first
        }

        // take the best one that is NOT equal to the recommended size
        return ranked.firstOrNull { it.first.name != recommendedSizeName }?.first
    }

    // Extension functions for List<Measurement>

    private fun fitDirection(fitRatio: Float): FitDirection {
        // API: fitRatio > 0 => body smaller => loose
        return if (fitRatio > 0) FitDirection.LOOSE else FitDirection.TIGHT
    }

    private fun fitTone(absFitRatio: Float): FitTone? = when {
        absFitRatio < 0.20 -> null
        absFitRatio < 0.45 -> FitTone.SLIGHT
        absFitRatio < 1.00 -> FitTone.MAY_FEEL
        else -> FitTone.EXACT
    }

    private fun directionText(direction: FitDirection): String = when (direction) {
        FitDirection.TIGHT -> "tight"
        FitDirection.LOOSE -> "loose"
    }

    private fun placeText(type: AiutaSizeFitRecommendation.Size.Measurement.MeasurementType): String = when (type) {
        AiutaSizeFitRecommendation.Size.Measurement.MeasurementType.CHEST_C -> "chest"
        AiutaSizeFitRecommendation.Size.Measurement.MeasurementType.WAIST_C -> "waist"
        AiutaSizeFitRecommendation.Size.Measurement.MeasurementType.HIP_C -> "hips"
        AiutaSizeFitRecommendation.Size.Measurement.MeasurementType.OVER_BUST -> "bust"
        else -> ""
    }

    private fun List<AiutaSizeFitRecommendation.Size.Measurement>.fitSummary(
        isRecommended: Boolean,
    ): String? {
        // collect relevant issues
        val issues = mapNotNull { m ->
            val absRatio = abs(m.fitRatio)
            val tone = fitTone(absRatio) ?: return@mapNotNull null
            val place = placeText(m.type).takeIf { it.isNotEmpty() } ?: return@mapNotNull null

            Triple(tone, fitDirection(m.fitRatio), place)
        }

        if (issues.isEmpty()) {
            return if (isRecommended) "Best fit" else null
        }

        val grouped = issues.groupBy { (tone, direction, _) ->
            FitGroupKey(direction = direction, tone = tone)
        }

        val phrases = grouped.map { (key, values) ->
            val places = values
                .map { it.third }
                .sorted()
                .joinToString(" and ")

            when (key.tone) {
                FitTone.SLIGHT -> "slightly ${directionText(key.direction)} in $places"
                FitTone.MAY_FEEL -> "may feel ${directionText(key.direction)} in $places"
                FitTone.EXACT -> "${directionText(key.direction)} in $places"
            }
        }

        val body = phrases.sorted().joinToString(
            separator = if (phrases.size == 2) " and " else ", ",
        )

        return body.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}
