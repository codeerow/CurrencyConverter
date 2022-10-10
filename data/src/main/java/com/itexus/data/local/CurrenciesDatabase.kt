package com.itexus.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.itexus.data.local.entity.CurrencyEntity

@Database(entities = [CurrencyEntity::class], version = 1)
abstract class CurrenciesDatabase : RoomDatabase() {
    abstract fun currenciesDao(): CurrenciesDao
}
