package com.aiuta.fashionsdk.benchmark

/** Application id of the sample app the journeys run against. */
internal const val TARGET_PACKAGE_NAME = "sample.tryon.kmp.android"

// Timeouts (ms). The Try On flow is network-bound, so screen waits are generous.
internal const val UI_TIMEOUT_MS = 5_000L
internal const val SCREEN_TIMEOUT_MS = 10_000L

/** First SDK screen appears only after the sample loads a product over network. */
internal const val FLOW_ENTER_TIMEOUT_MS = 20_000L

/** Try On generation is a backend round-trip; wait long before giving up. */
internal const val GENERATION_TIMEOUT_MS = 120_000L

/** Upper bound on onboarding pages to click through. */
internal const val MAX_ONBOARDING_PAGES = 6

// Test tags live in :internal:internal-benchmark-tags (com.aiuta.fashionsdk.benchmark.tags.AiutaTestTags),
// shared with the SDK and sample so the strings can never drift.
