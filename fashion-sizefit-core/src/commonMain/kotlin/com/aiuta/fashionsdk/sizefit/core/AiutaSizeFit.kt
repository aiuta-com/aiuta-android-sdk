package com.aiuta.fashionsdk.sizefit.core

/**
 * Main interface for the Aiuta SizeFit service that provides size recommendations
 * based on user's body measurements and shape.
 *
 * This service uses the NaizFit API to calculate personalized size recommendations
 * for clothing items based on the user's physical characteristics and the product's
 * size chart.
 *
 * **Important**: This interface implements [AutoCloseable]. When you're done using
 * the instance, call [close] to release network resources (HTTP client, connection pools).
 *
 * ```kotlin
 * val sizeFit = aiutaSizeFit {
 *     aiuta = myAiutaInstance
 *     apiKey = "your-api-key"
 *     partitionHeader = "1"
 * }
 *
 * try {
 *     val config = AiutaSizeFitConfig(
 *         age = 30,
 *         height = 165,
 *         weight = 68,
 *         gender = AiutaSizeFitConfig.Gender.FEMALE,
 *         hipShape = AiutaSizeFitConfig.HipShape.NORMAL,
 *         bellyShape = AiutaSizeFitConfig.BellyShape.NORMAL
 *     )
 *
 *     val recommendation = sizeFit.recommendSize("product-size-chart-code", config)
 *     println("Recommended size: ${recommendation.recommendedSizeName}")
 * } finally {
 *     sizeFit.close()
 * }
 * ```
 *
 * @see AiutaSizeFitConfig
 * @see AiutaSizeFitRecommendation
 * @see aiutaSizeFit
 */
public interface AiutaSizeFit : AutoCloseable {

    /**
     * Get a size recommendation based on user's body measurements and shape.
     *
     * This method sends a request to the NaizFit API with the user's physical characteristics
     * and receives a personalized size recommendation along with detailed measurements and
     * fit information for all available sizes.
     *
     * @param code The unique size chart code associated with the product for which
     *             the recommendation is requested (e.g., "jeans-slimfit-blue-2023")
     * @param config User's body measurements and shape configuration
     * @return [AiutaSizeFitRecommendation] containing the recommended size name,
     *         detailed measurements for all sizes, and the user's calculated body measurements
     * @throws Exception if the API request fails or the size chart is not found
     *
     * @see AiutaSizeFitConfig
     * @see AiutaSizeFitRecommendation
     */
    public suspend fun recommendSize(
        code: String,
        config: AiutaSizeFitConfig,
    ): AiutaSizeFitRecommendation
}
