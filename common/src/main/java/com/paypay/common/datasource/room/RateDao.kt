package com.paypay.common.datasource.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RateDao {

    @Query("SELECT * FROM rateroomentity WHERE source = :currency ORDER BY source ASC")
    suspend fun getAllFromCurrency(currency: String): RateRoomEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rateRoomEntity: RateRoomEntity)
}