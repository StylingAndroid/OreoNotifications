package com.stylingandroid.oreo.notifications.messenger

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.stylingandroid.oreo.notifications.ifAtLeast

class NotificationChannelBuilder(
        context: Context,
        private val channelIds: List<String>,
        private val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
) {

    fun ensureChannelsExist(createChannel: (channelId: String) -> NotificationChannel?) =
            ifAtLeast(Build.VERSION_CODES.O) {
                notificationManager.ensureChannelsExist(createChannel)
            }

    @TargetApi(Build.VERSION_CODES.O)
    private fun NotificationManager.ensureChannelsExist(createChannel: (channelId: String) -> NotificationChannel?) {
        channelIds
                .filter { !notificationChannelIds().contains(it) }
                .forEach {
                    createChannel(it)?.also {
                        notificationManager.createNotificationChannel(it)
                    }
                }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun NotificationManager.notificationChannelIds() =
            notificationChannels.map { it.id }

}
