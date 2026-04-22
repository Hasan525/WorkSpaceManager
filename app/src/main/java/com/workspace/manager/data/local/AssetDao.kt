package com.workspace.manager.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {

    @Query("SELECT * FROM assets ORDER BY sortOrder ASC")
    fun observeAllAssets(): Flow<List<AssetEntity>>

    @Query("SELECT * FROM assets WHERE isPendingSync = 1")
    suspend fun getPendingSyncAssets(): List<AssetEntity>

    @Query("SELECT * FROM assets WHERE id = :id")
    suspend fun getAssetById(id: String): AssetEntity?

    @Upsert
    suspend fun upsertAsset(asset: AssetEntity)

    @Transaction
    suspend fun upsertAssets(assets: List<AssetEntity>) {
        assets.forEach { upsertAsset(it) }
    }

    @Query("UPDATE assets SET isPendingSync = 0 WHERE id = :id")
    suspend fun markSynced(id: String)

    @Query("DELETE FROM assets WHERE id = :id")
    suspend fun deleteAsset(id: String)

    @Query("UPDATE assets SET rotationAngle = :angle, updatedAt = :updatedAt, isPendingSync = 1 WHERE id = :id")
    suspend fun updateRotation(id: String, angle: Float, updatedAt: Long)

    @Query("UPDATE assets SET sortOrder = :sortOrder WHERE id = :id")
    suspend fun updateSortOrder(id: String, sortOrder: Long)
}
