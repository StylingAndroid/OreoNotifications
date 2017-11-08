package com.stylingandroid.oreo.notifications.messenger

import android.app.Notification
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.stylingandroid.oreo.notifications.R
import com.stylingandroid.oreo.notifications.bindSharedPreference
import com.stylingandroid.oreo.notifications.safeContext

class NotificationBuilder(
        private val context: Context,
        private val safeContext: Context = context.safeContext(),
        private val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(safeContext)
) {

    private var notificationId: Int by bindSharedPreference(context, KEY_NOTIFICATION_ID, 0)

    fun sendBundledNotification(message: Message) =
            with(notificationManager) {
                notify(notificationId++, buildNotification(message))
                notify(SUMMARY_ID, buildSummary(message))
            }

    private fun buildNotification(message: Message): Notification =
            with(NotificationCompat.Builder(context)) {
                message.apply {
                    setContentTitle(sender)
                    setContentText(text)
                    setWhen(timestamp.toEpochMilli())
                }
                setSmallIcon(R.drawable.ic_message)
                setShowWhen(true)
                setGroup(GROUP_KEY)
                build()
            }

    private fun buildSummary(message: Message): Notification =
            with(NotificationCompat.Builder(context)) {
                setContentTitle(SUMMARY_TITLE)
                setContentText(SUMMARY_TEXT)
                setWhen(message.timestamp.toEpochMilli())
                setSmallIcon(R.drawable.ic_message)
                setShowWhen(true)
                setGroup(GROUP_KEY)
                setGroupSummary(true)
                build()
            }

    companion object {
        private const val KEY_NOTIFICATION_ID = "KEY_NOTIFICATION_ID"
        private const val GROUP_KEY = "Messenger"
        private const val SUMMARY_ID = 0
        private const val SUMMARY_TITLE = "Nougat Messenger"
        private const val SUMMARY_TEXT = "You have unread messages"
    }
}
