package com.itexus.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.itexus.data.local.entity.CurrencyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrenciesDao {

    @Insert
    suspend fun insertAll(entities: List<CurrencyEntity>)

    @Insert(onConflict = REPLACE)
    suspend fun insert(entity: CurrencyEntity)

    @Query("SELECT * FROM currencies")
    fun readAll(): Flow<List<CurrencyEntity>>
}
