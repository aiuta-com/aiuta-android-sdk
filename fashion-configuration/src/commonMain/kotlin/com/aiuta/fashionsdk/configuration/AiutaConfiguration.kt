package com.aiuta.fashionsdk.configuration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.annotations.AiutaDsl
import com.aiuta.fashionsdk.configuration.debug.AiutaDebugSettings
import com.aiuta.fashionsdk.configuration.debug.DefaultAiutaDebugSettings
import com.aiuta.fashionsdk.configuration.experimental.AiutaExperimentalSettings
import com.aiuta.fashionsdk.configuration.experimental.DefaultAiutaExperimentalSettings
import com.aiuta.fashionsdk.configuration.features.AiutaFeatures
import com.aiuta.fashionsdk.configuration.internal.analytic.sendConfigurationEvent
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription
import com.aiuta.fashionsdk.configuration.internal.validation.features.validateWithSettings
import com.aiuta.fashionsdk.configuration.internal.validation.theme.validateWithSettings
import com.aiuta.fashionsdk.configuration.ui.AiutaUserInterfaceConfiguration
import com.aiuta.fashionsdk.internal.analytics.InternalAiutaAnalytic
import com.aiuta.fashionsdk.internal.analytics.internalAiutaAnalytic

/**
 * Main configuration class for the Aiuta SDK.
 *
 * This immutable configuration class contains all the settings needed to customize
 * the behavior and appearance of the Aiuta SDK. It includes feature toggles,
 * UI theming, debug settings, and the core Aiuta instance.
 *
 * ```kotlin
 * val configuration = aiutaConfiguration {
 *     aiuta = aiuta {
 *         authenticationStrategy = ...
 *         platformContext = context
 *     }
 *
 *     features {
 *         ...
 *     }
 *
 *     userInterface {
 *         theme = ...
 *         actions = ...
 *     }
 * }
 * ```
 *
 * @property aiuta The core Aiuta SDK instance
 * @property debugSettings Debug and validation settings for development
 * @property experimentalSettings Opt-in settings for experimental or non-default behavior
 * @property features Feature configuration defining which SDK features are enabled
 * @property userInterface UI configuration including theme and actions
 *
 * @see Aiuta
 * @see AiutaFeatures
 * @see AiutaUserInterfaceConfiguration
 * @see AiutaDebugSettings
 * @see AiutaExperimentalSettings
 */
@Immutable
public class AiutaConfiguration(
    public val aiuta: Aiuta,
    public val debugSettings: AiutaDebugSettings,
    public val experimentalSettings: AiutaExperimentalSettings,
    public val features: AiutaFeatures,
    public val userInterface: AiutaUserInterfaceConfiguration,
) {
    internal val aiutaAnalytic: InternalAiutaAnalytic by lazy { aiuta.internalAiutaAnalytic }

    /**
     * Builder class for creating [AiutaConfiguration] instances with DSL-style configuration.
     *
     * This builder ensures all required properties are set and validates the configuration
     * before creating the final [AiutaConfiguration] instance.
     *
     */
    @AiutaDsl
    public class Builder {
        /**
         * The core Aiuta SDK instance.
         */
        public var aiuta: Aiuta? = null

        /**
         * Debug and validation settings.
         * If not set, [DefaultAiutaDebugSettings] will be used.
         */
        public var debugSettings: AiutaDebugSettings? = null

        /**
         * Experimental settings for opt-in non-default behavior.
         * If not set, [DefaultAiutaExperimentalSettings] will be used.
         */
        public var experimentalSettings: AiutaExperimentalSettings? = null

        /**
         * Feature configuration defining enabled SDK features.
         */
        public var features: AiutaFeatures? = null

        /**
         * User interface configuration including theme and actions.
         */
        public var userInterface: AiutaUserInterfaceConfiguration? = null

        /**
         * Creates an [AiutaConfiguration] instance with the configured properties.
         *
         * This method validates all required properties and performs configuration
         * validation using the debug settings.
         *
         * @return Configured [AiutaConfiguration] instance
         * @throws IllegalArgumentException if required properties are not set
         */
        public fun build(): AiutaConfiguration {
            val parentClass = "AiutaTryOnConfiguration"

            val innerDebugSettings = debugSettings ?: DefaultAiutaDebugSettings
            val innerExperimentalSettings = experimentalSettings ?: DefaultAiutaExperimentalSettings

            val internalAiuta = aiuta.checkNotNullWithDescription(
                parentClass = parentClass,
                property = "aiuta",
            )
            val internalFeatures = features.checkNotNullWithDescription(
                parentClass = parentClass,
                property = "features",
            ).also { features ->
                features.validateWithSettings(
                    logger = internalAiuta.logger,
                    debugSettings = innerDebugSettings,
                )
            }
            val innerUserInterface = userInterface.checkNotNullWithDescription(
                parentClass = parentClass,
                property = "userInterface",
            ).also { configuration ->
                configuration.theme.validateWithSettings(
                    logger = internalAiuta.logger,
                    debugSettings = innerDebugSettings,
                )
            }

            return AiutaConfiguration(
                aiuta = internalAiuta,
                debugSettings = innerDebugSettings,
                experimentalSettings = innerExperimentalSettings,
                features = internalFeatures,
                userInterface = innerUserInterface,
            ).also {
                it.sendConfigurationEvent()
            }
        }
    }
}

/**
 * Creates an [AiutaConfiguration] instance using DSL-style configuration.
 *
 * This is the recommended way to create [AiutaConfiguration] instances as it provides
 * a clean and readable configuration syntax with validation.
 *
 * ```kotlin
 * val configuration = aiutaConfiguration {
 *     aiuta = aiuta {
 *         authenticationStrategy = ...
 *         platformContext = context
 *     }
 *
 *     features {
 *         ...
 *     }
 *
 *     userInterface {
 *         theme = ...
 *         actions = ...
 *     }
 *
 *      debugSettings = AiutaDebugSettings(
 *         emptyStringsPolicy = AiutaValidationPolicy.WARNING,
 *         listSizePolicy = AiutaValidationPolicy.WARNING
 *     )
 * }
 * ```
 *
 * @param block Configuration block for setting up the [AiutaConfiguration] instance
 * @return Configured [AiutaConfiguration] instance
 * @throws IllegalArgumentException if required properties are not set in the block
 * @see AiutaConfiguration.Builder
 */
public inline fun aiutaConfiguration(
    block: AiutaConfiguration.Builder.() -> Unit,
): AiutaConfiguration = AiutaConfiguration.Builder().apply(block).build()

/**
 * Remembers an [AiutaConfiguration] instance within a Compose function.
 *
 * This composable function creates and remembers an [AiutaConfiguration] instance,
 * ensuring it's only created once and reused across recompositions.
 *
 * ```kotlin
 * @Composable
 * fun MyScreen() {
 *     val configuration = rememberAiutaConfiguration {
 *         aiuta = // Your Aiuta instance
 *         features {
 *             ...
 *         }
 *         userInterface {
 *             theme = ...
 *             actions = ...
 *         }
 *     }
 *
 *     // Use configuration below
 * }
 * ```
 *
 * @param block Configuration block for setting up the [AiutaConfiguration] instance
 * @return Remembered [AiutaConfiguration] instance
 * @see remember
 * @see aiutaConfiguration
 */
@Composable
public fun rememberAiutaConfiguration(
    block: AiutaConfiguration.Builder.() -> Unit,
): AiutaConfiguration = remember { aiutaConfiguration(block) }
