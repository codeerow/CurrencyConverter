package com.itexus.data.local

import com.itexus.data.local.entity.CurrencyEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CurrenciesDao(private val realm: Realm) {

    suspend fun insertAll(entities: List<CurrencyEntity>) {
        realm.write {
            entities.forEach {
                copyToRealm(it, UpdatePolicy.ALL)
            }
        }
    }

    fun readAll(): Flow<List<CurrencyEntity>> {
        return realm.query(CurrencyEntity::class)
            .find()
            .asFlow()
            .map { it.list }
    }

    fun read(id: String): Flow<CurrencyEntity?> {
        return realm.query(
            CurrencyEntity::class,
            query = "id == $0",
            id,
        ).first().asFlow().map { it.obj }
    }
}
