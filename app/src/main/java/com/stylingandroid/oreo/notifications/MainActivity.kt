package com.stylingandroid.oreo.notifications

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stylingandroid.oreo.notifications.messenger.ServiceScheduler

class MainActivity : AppCompatActivity() {
    private val serviceScheduler: ServiceScheduler by lazyFast {
        ServiceScheduler(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serviceScheduler.takeIf { it.isEnabled }?.apply {
            startService()
        }
    }
}
