package com.aiuta.fashionsdk.tryon.compose.data.internal.database.builder

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.aiuta.fashionsdk.context.AiutaPlatformContext
import com.aiuta.fashionsdk.tryon.compose.data.internal.database.AppDatabase
import com.aiuta.fashionsdk.tryon.compose.data.internal.database.converters.ListStringsConverter
import com.aiuta.fashionsdk.tryon.compose.data.internal.database.converters.PowerByStickerConverter
import com.aiuta.fashionsdk.tryon.compose.data.internal.database.converters.TryOnModelsCategoriesConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

internal expect fun getDatabaseBuilder(
    platformContext: AiutaPlatformContext,
): RoomDatabase.Builder<AppDatabase>

internal fun buildRoomDatabase(platformContext: AiutaPlatformContext): AppDatabase = getDatabaseBuilder(platformContext)
    .setDriver(BundledSQLiteDriver())
    .setQueryCoroutineContext(Dispatchers.IO)
    // Config
    .addTypeConverter(PowerByStickerConverter())
    .addTypeConverter(TryOnModelsCategoriesConverter())
    .addTypeConverter(ListStringsConverter())
    // Fallback
    .fallbackToDestructiveMigration(true)
    .build()
