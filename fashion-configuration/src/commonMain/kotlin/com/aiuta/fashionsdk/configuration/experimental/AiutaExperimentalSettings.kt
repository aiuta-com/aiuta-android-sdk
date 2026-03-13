package com.aiuta.fashionsdk.configuration.experimental

/**
 * Experimental settings for the Aiuta SDK.
 *
 * This class contains opt-in settings for features that are not yet stable
 * or that change default platform behavior. Use with caution — these settings
 * may change or be removed in future releases.
 *
 * ```kotlin
 * val configuration = aiutaConfiguration {
 *     experimentalSettings = AiutaExperimentalSettings(
 *         shouldOpenLinksInCustomTab = true
 *     )
 *     // other configuration...
 * }
 * ```
 *
 * @property shouldOpenLinksInCustomTab When `true`, links opened by the SDK will use
 * Android Custom Tabs instead of the default system browser intent. This prevents
 * Android App Links from intercepting URLs and always opens them inside the app.
 * Defaults to `false`.
 *
 * @see DefaultAiutaExperimentalSettings
 */
public class AiutaExperimentalSettings(
    public val shouldOpenLinksInCustomTab: Boolean,
)

/**
 * Default experimental settings for the Aiuta SDK.
 *
 * ```kotlin
 * val configuration = aiutaConfiguration {
 *     // experimentalSettings will automatically use DefaultAiutaExperimentalSettings
 *     features { /* ... */ }
 *     userInterface { /* ... */ }
 * }
 * ```
 *
 * @see AiutaExperimentalSettings
 */
public val DefaultAiutaExperimentalSettings: AiutaExperimentalSettings by lazy {
    AiutaExperimentalSettings(
        shouldOpenLinksInCustomTab = false,
    )
}
