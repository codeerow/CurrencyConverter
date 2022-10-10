package com.itexus.logic.core

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

fun tickerFlow(millis: Long, initialDelayMillis: Long = 0) = flow {
    delay(timeMillis = initialDelayMillis)
    while (true) {
        emit(Unit)
        delay(timeMillis = millis)
    }
}
