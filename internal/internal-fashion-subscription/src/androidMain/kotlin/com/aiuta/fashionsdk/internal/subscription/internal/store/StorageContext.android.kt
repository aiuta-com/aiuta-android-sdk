package com.aiuta.fashionsdk.internal.subscription.internal.store

import com.aiuta.fashionsdk.context.AiutaPlatformContext

internal actual fun AiutaPlatformContext.toStorageContext(): AiutaPlatformContext = applicationContext ?: this
