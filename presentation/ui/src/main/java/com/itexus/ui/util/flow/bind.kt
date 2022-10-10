package com.itexus.ui.util.flow

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber

fun <T : Any> Flow<T>.bind(
    lifecycleOwner: LifecycleOwner,
    onError: suspend (Throwable) -> Unit = { Timber.tag(javaClass.name).e(it.stackTraceToString()) },
    onNext: suspend (T) -> Unit = {},
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            catch { onError(it) }.collect(onNext)
        }
    }
}
