package com.aiuta.fashionsdk.internal.analytics.internal.worker

import com.aiuta.fashionsdk.context.AiutaPlatformContext
import com.aiuta.fashionsdk.internal.analytic.BuildKonfig
import com.aiuta.fashionsdk.internal.analytics.internal.installation.Installation
import com.aiuta.fashionsdk.internal.analytics.internal.model.AnalyticEnvironment
import com.aiuta.fashionsdk.internal.analytics.internal.utils.AnalyticConfig

internal actual suspend fun createAnalyticEnvironment(
    platformContext: AiutaPlatformContext,
): AnalyticEnvironment = try {
    AnalyticEnvironment(
        platform = AnalyticConfig.PLATFORM_WEB,
        sdkVersion = BuildKonfig.VERSION_NAME,
        hostId = AnalyticConfig.DEFAULT_HOST_ID,
        hostVersion = AnalyticConfig.DEFAULT_HOST_VERSION,
        installationId = Installation.id(platformContext),
    )
} catch (e: Exception) {
    AnalyticEnvironment.DEFAULT
}
