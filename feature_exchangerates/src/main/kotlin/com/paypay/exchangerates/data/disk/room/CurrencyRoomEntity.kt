package com.paypay.exchangerates.data.disk.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrencyRoomEntity(
    @PrimaryKey val code: String,
    @ColumnInfo val label: String
)