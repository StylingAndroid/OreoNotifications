package com.stylingandroid.oreo.notifications

import android.content.Context
import android.support.v4.content.ContextCompat

fun <T> lazyFast(operation: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE) {
    operation()
}

fun Context.safeContext(): Context =
        takeUnless { isDeviceProtectedStorage }?.let {
            it.applicationContext.let {
                ContextCompat.createDeviceProtectedStorageContext(it) ?: it
            }
        } ?: this
