package com.aiuta.fashionsdk.tryon.compose.data.internal.entity.local.subscription

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.aiuta.fashionsdk.tryon.compose.data.internal.database.converters.PowerByStickerConverter
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.remote.subscription.features.PoweredByStickerFeature

// Unused since the subscription details moved to internal-fashion-subscription.
// Kept so the Room schema doesn't change: a version bump would fall back to a
// destructive migration and wipe the users' history.
@Entity(tableName = "subscription_details")
internal class SubscriptionDetailsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val etag: String? = null,
    @TypeConverters(PowerByStickerConverter::class)
    val poweredByStickerFeature: PoweredByStickerFeature? = null,
)
