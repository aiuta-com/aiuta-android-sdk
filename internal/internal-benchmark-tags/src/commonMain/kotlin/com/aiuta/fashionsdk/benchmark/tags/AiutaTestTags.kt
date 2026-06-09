package com.aiuta.fashionsdk.benchmark.tags

/**
 * Single source of truth for Compose `testTag`s used to drive the SDK from the
 * Baseline Profile generator and Macrobenchmarks (`:internal:benchmark`).
 *
 * Tags are set on composables in the SDK and the sample, and read by the
 * benchmark journey via UiAutomator's `By.res(...)` (the host app exposes them
 * as resource ids through `testTagsAsResourceId`). `testTag` is inert at
 * runtime, so shipping these constants is harmless.
 */
public object AiutaTestTags {

    // Sample flow selector — set in :samples:tryon-kmp.
    public const val FLOW_SELECTOR: String = "flow_selector"
    public const val TRY_ON_FLOW_BUTTON: String = "tryon_flow_button"
    public const val HISTORY_FLOW_BUTTON: String = "history_flow_button"

    // SDK Try On journey — set in :fashion-tryon-compose.
    public const val WELCOME_START: String = "aiuta_welcome_start"
    public const val ONBOARDING_NEXT: String = "aiuta_onboarding_next"
    public const val SELECT_MODEL: String = "aiuta_select_model"
    public const val MODEL_TRY_ON: String = "aiuta_model_try_on"
    public const val GENERATE: String = "aiuta_generate"
    public const val RESULT_SCREEN: String = "aiuta_result_screen"
    public const val HISTORY_APP_BAR: String = "aiuta_history_app_bar"
    public const val HISTORY_GRID: String = "aiuta_history_grid"
}
