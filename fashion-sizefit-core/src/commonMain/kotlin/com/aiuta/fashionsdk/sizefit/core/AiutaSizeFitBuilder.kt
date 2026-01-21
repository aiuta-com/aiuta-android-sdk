package com.aiuta.fashionsdk.sizefit.core

import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.annotations.AiutaDsl
import com.aiuta.fashionsdk.sizefit.core.internal.AiutaSizeFitImpl

/**
 * Builder class for creating [AiutaSizeFit] instances with DSL-style configuration.
 *
 * This builder ensures all required properties are set before creating an [AiutaSizeFit] instance.
 *
 * ```kotlin
 * val sizeFit = AiutaSizeFitBuilder()
 *     .apply {
 *         aiuta = myAiutaInstance
 *         apiKey = "your-api-key"
 *         partitionHeader = "1"
 *     }
 *     .build()
 * ```
 *
 * Or using the DSL function:
 * ```kotlin
 * val sizeFit = aiutaSizeFit {
 *     aiuta = myAiutaInstance
 *     apiKey = "your-api-key"
 *     partitionHeader = "1"
 * }
 * ```
 *
 * @see AiutaSizeFit
 */
@AiutaDsl
public class AiutaSizeFitBuilder {
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
    block: AiutaSizeFitBuilder.() -> Unit,
): AiutaSizeFit = AiutaSizeFitBuilder().apply(block).build()
