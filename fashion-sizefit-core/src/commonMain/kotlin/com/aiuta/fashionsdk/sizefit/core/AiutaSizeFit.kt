package com.aiuta.fashionsdk.sizefit.core

import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.annotations.AiutaDsl
import com.aiuta.fashionsdk.sizefit.core.internal.AiutaSizeFitImpl

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

    @AiutaDsl
    public class Builder {
        /**
         * Aiuta SDK instance required for logging and platform context.
         */
        public var aiuta: Aiuta? = null

        /**
         * API key for authentication with the NaizFit API.
         * This key is sent in the X-API-Key header.
         */
        public var apiKey: String? = null

        /**
         * Partition header value for multi-tenant support.
         * Maximum length: 8 characters.
         * This value is sent in the X-Partition header.
         */
        public var partitionHeader: String? = null

        /**
         * Creates an [AiutaSizeFit] instance with the configured properties.
         *
         * @return Configured [AiutaSizeFit] instance
         * @throws IllegalArgumentException if required properties are not set or invalid
         */
        public fun build(): AiutaSizeFit {
            val aiutaInstance = aiuta.checkNotNullWithDescription(
                property = "aiuta",
                methodToCall = "setAiuta()",
            )

            val apiKeyValue = apiKey.checkNotNullWithDescription(
                property = "apiKey",
                methodToCall = "setApiKey()",
            )

            val partitionHeaderValue = partitionHeader.checkNotNullWithDescription(
                property = "partitionHeader",
                methodToCall = "setPartitionHeader()",
            )

            require(apiKeyValue.isNotBlank()) {
                "AiutaSizeFitBuilder: apiKey cannot be blank"
            }

            require(partitionHeaderValue.isNotBlank()) {
                "AiutaSizeFitBuilder: partitionHeader cannot be blank"
            }

            require(partitionHeaderValue.length <= 8) {
                "AiutaSizeFitBuilder: partitionHeader must be at most 8 characters, got ${partitionHeaderValue.length}"
            }

            return AiutaSizeFitImpl.create(
                aiuta = aiutaInstance,
                apiKey = apiKeyValue,
                partitionHeader = partitionHeaderValue,
            )
        }

        private companion object {
            fun <T> T?.checkNotNullWithDescription(
                property: String,
                methodToCall: String,
            ): T = checkNotNull(
                value = this,
                lazyMessage = {
                    propertyIsNull(
                        property = property,
                        methodToCall = methodToCall,
                    )
                },
            )

            fun propertyIsNull(
                property: String,
                methodToCall: String,
            ): String = "AiutaSizeFitBuilder: $property is null, therefore cannot build AiutaSizeFit. " +
                "Please, call $methodToCall before build()"
        }
    }
}

/**
 * Creates an [AiutaSizeFit] instance using DSL-style configuration.
 *
 * This is the recommended way to create [AiutaSizeFit] instances as it provides
 * a clean and readable configuration syntax.
 *
 * ```kotlin
 * val sizeFit = aiutaSizeFit {
 *     aiuta = myAiutaInstance
 *     apiKey = "your-api-key"
 *     partitionHeader = "1"
 * }
 * ```
 *
 * @param block Configuration block for setting up the [AiutaSizeFit] instance
 * @return Configured [AiutaSizeFit] instance
 * @throws IllegalArgumentException if required properties are not set or invalid
 */
public inline fun aiutaSizeFit(
    block: AiutaSizeFit.Builder.() -> Unit,
): AiutaSizeFit = AiutaSizeFit.Builder().apply(block).build()
