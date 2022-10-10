package com.itexus.domain.repo

import android.content.Context
import android.content.Context.MODE_PRIVATE

class ExchangesCountRepository(
    context: Context,
) {

    private val settings = context.getSharedPreferences("exchanges", MODE_PRIVATE)

    fun addExchangesCount() {
        val editor = settings.edit()
        editor.clear()
        editor.putInt("exchanges", getExchangesCount().inc())
        editor.commit()
    }

    private fun getExchangesCount(): Int = settings.getInt("exchanges", 0)

    fun getCommissionFee() = if (getExchangesCount() > MAX_FREE_EXCHANGES) {
        COMMISSION_FEE
    } else {
        0.0
    }

    companion object {
        private const val MAX_FREE_EXCHANGES = 5
        private const val COMMISSION_FEE = 0.007
    }
}
