package com.stylingandroid.oreo.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.stylingandroid.oreo.notifications.messenger.ServiceScheduler

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        intent.takeIf { ACTIONS.contains(it.action) }?.also {
            ServiceScheduler(context).takeIf { it.isEnabled }?.apply {
                startService()
            }
        }
    }

    companion object {
        private val ACTIONS = listOf(Intent.ACTION_BOOT_COMPLETED, Intent.ACTION_LOCKED_BOOT_COMPLETED)
    }
}
