package com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.subscription.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.local.subscription.SubscriptionDetailsEntity

@Dao
internal interface SubscriptionDetailsDao {
    @Query("SELECT * from subscription_details LIMIT :limit")
    suspend fun getAll(limit: Int): List<SubscriptionDetailsEntity>

    @Query("SELECT etag from subscription_details LIMIT :limit")
    suspend fun getAllEtags(limit: Int): List<String>

    @Query("DELETE from subscription_details")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: SubscriptionDetailsEntity)
}

@Transaction
internal suspend fun SubscriptionDetailsDao.replaceAll(clientConfig: SubscriptionDetailsEntity) {
    deleteAll()
    insert(clientConfig)
}
