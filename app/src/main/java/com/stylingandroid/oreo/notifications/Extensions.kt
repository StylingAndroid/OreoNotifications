package com.stylingandroid.oreo.notifications

import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat

fun <T> lazyFast(operation: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE) {
    operation()
}

fun Context.safeContext(): Context =
        takeUnless { isDeviceProtectedStorage }?.run {
            applicationContext.let {
                ContextCompat.createDeviceProtectedStorageContext(it) ?: it
            }
        } ?: this

fun ifAtLeast(version: Int, function: () -> Unit) {
    if (Build.VERSION.SDK_INT >= version) {
        function()
    }
}
