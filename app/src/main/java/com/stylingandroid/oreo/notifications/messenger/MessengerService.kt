package com.stylingandroid.oreo.notifications.messenger

import android.content.Context
import com.google.android.gms.gcm.GcmNetworkManager
import com.google.android.gms.gcm.GcmTaskService
import com.google.android.gms.gcm.TaskParams
import com.stylingandroid.oreo.notifications.lazyFast
import com.stylingandroid.oreo.notifications.safeContext

class MessengerService : GcmTaskService() {
    private val safeContext: Context by lazyFast { safeContext() }

    private val messenger: Messenger by lazyFast { Messenger(safeContext) }

    private val serviceScheduler: ServiceScheduler by lazyFast { ServiceScheduler(safeContext) }

    private val notificationBuilder: NotificationBuilder by lazyFast {
        NotificationBuilder(safeContext)
    }

    override fun onRunTask(taskParams: TaskParams?): Int {
        with(messenger.generateNewMessage()) {
            println("Message: $this")
            notificationBuilder.sendBundledNotification(this)
            serviceScheduler.scheduleService()
        }
        return GcmNetworkManager.RESULT_SUCCESS
    }

}
