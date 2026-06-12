package com.aiuta.fashionsdk.configuration.debug

/**
 * Debug and validation settings for the Aiuta SDK.
 *
 * This class controls how the SDK handles validation and debugging during
 * development. It allows you to configure validation policies for different
 * types of configuration issues.
 *
 * ```kotlin
 * val debugSettings = AiutaDebugSettings(
 *     emptyStringsPolicy = AiutaValidationPolicy.FATAL,
 *     listSizePolicy = AiutaValidationPolicy.WARNING,
 *     modeFallbackPolicy = AiutaValidationPolicy.WARNING
 * )
 *
 * val configuration = aiutaConfiguration {
 *     debugSettings = debugSettings
 *     // other configuration...
 * }
 * ```
 *
 * @property emptyStringsPolicy Policy for handling empty string validations
 * @property listSizePolicy Policy for handling list size validations
 * @property modeFallbackPolicy Policy applied when an active mode is missing a value
 * and the SDK falls back to the default (GENERAL) one
 * @see AiutaValidationPolicy
 * @see DefaultAiutaDebugSettings
 */
public class AiutaDebugSettings(
    public val emptyStringsPolicy: AiutaValidationPolicy,
    public val listSizePolicy: AiutaValidationPolicy,
    public val modeFallbackPolicy: AiutaValidationPolicy,
)

/**
 * Default debug settings for the Aiuta SDK.
 *
 * This provides sensible default values for debug settings that are suitable
 * for most development scenarios. All validation policies are set to WARNING,
 * which means issues will be logged but won't crash the application.
 *
 * ```kotlin
 * // Using default settings
 * val configuration = aiutaConfiguration {
 *     // debugSettings will automatically use DefaultAiutaDebugSettings
 *     features { /* ... */ }
 *     userInterface { /* ... */ }
 * }
 *
 * // Explicitly using default settings
 * val configuration = aiutaConfiguration {
 *     debugSettings = DefaultAiutaDebugSettings
 *     // other configuration...
 * }
 * ```
 *
 * @see AiutaDebugSettings
 * @see AiutaValidationPolicy.WARNING
 */
public val DefaultAiutaDebugSettings: AiutaDebugSettings by lazy {
    AiutaDebugSettings(
        emptyStringsPolicy = AiutaValidationPolicy.WARNING,
        listSizePolicy = AiutaValidationPolicy.WARNING,
        modeFallbackPolicy = AiutaValidationPolicy.WARNING,
    )
}
