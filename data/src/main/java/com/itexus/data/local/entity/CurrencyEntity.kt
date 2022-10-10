package com.itexus.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class CurrencyEntity(
    @PrimaryKey @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "value") val value: Double = 0.0,
)
