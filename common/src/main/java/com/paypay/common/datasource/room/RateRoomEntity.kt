package com.paypay.common.datasource.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
@TypeConverters(RateListConverter::class)
data class RateRoomEntity(
    @PrimaryKey val source: String,
    @ColumnInfo val rates: List<RateEntity>
)

@Entity
data class RateEntity(
    @ColumnInfo val currency: String,
    @ColumnInfo val rate: Double
)