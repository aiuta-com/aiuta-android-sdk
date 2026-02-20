package com.aiuta.fashionsdk.network.utils

import com.aiuta.fashionsdk.network.internal.KtorHttpClientFactory

internal val authorizedPaths = setOf(
    "${KtorHttpClientFactory.DEFAULT_ENCODED_PATH}sku_images_operations",
)
