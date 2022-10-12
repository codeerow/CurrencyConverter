package com.itexus.data.local.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.bson.types.Decimal128
import org.bson.types.Decimal128.POSITIVE_ZERO

class CurrencyEntity : RealmObject {
    @PrimaryKey
    var id: String = ""
    var name: String = ""
    var value: Decimal128 = POSITIVE_ZERO
}
