package com.paypay.common.datasource.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CurrencyRoomEntity::class, RateRoomEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao
    abstract fun rateDao(): RateDao
}