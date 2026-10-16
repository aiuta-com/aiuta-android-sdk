package com.aiuta.fashionsdk.tryon.core.data.datasource.subscription

import com.aiuta.fashionsdk.tryon.core.data.datasource.subscription.models.OperationDelaysDetails

internal interface FashionSubscriptionDetailsDataSource {
    suspend fun getOperationDelaysDetails(): OperationDelaysDetails
}
