package com.aiuta.fashionsdk.tryon.compose.data.internal.database.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.remote.subscription.features.PoweredByStickerFeature

@ProvidedTypeConverter
internal class PowerByStickerConverter : BaseSerializedConverter() {
    @TypeConverter
    fun restoreTryOnModelsCategories(rawString: String?): PoweredByStickerFeature? = restore(rawString)

    @TypeConverter
    fun saveTryOnModelsCategories(config: PoweredByStickerFeature): String? = save(config)
}
