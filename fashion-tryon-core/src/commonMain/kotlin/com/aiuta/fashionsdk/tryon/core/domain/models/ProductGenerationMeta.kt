package com.aiuta.fashionsdk.tryon.core.domain.models

import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsMode

/**
 * Optional meta information passed alongside a [ProductGenerationContainer].
 *
 * Carries extra, non-essential context about the generation flow that is used
 * to enrich analytics events emitted from the core layer (which has no access
 * to the UI controller). Currently holds the resolved try-on [mode].
 *
 * @property mode Active try-on mode for the flow, or `null` when not applicable.
 */
public class ProductGenerationMeta(
    public val mode: AiutaAnalyticsMode? = null,
)
