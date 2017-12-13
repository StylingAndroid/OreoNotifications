package com.stylingandroid.oreo.notifications.messenger

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.app.TaskStackBuilder
import com.stylingandroid.oreo.notifications.R
import com.stylingandroid.oreo.notifications.SecondActivity
import com.stylingandroid.oreo.notifications.bindSharedPreference
import com.stylingandroid.oreo.notifications.safeContext
import java.util.*

class NotificationBuilder(
        private val context: Context,
        private val safeContext: Context = context.safeContext(),
        private val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(safeContext),
        private val channelBuilder: NotificationChannelBuilder = NotificationChannelBuilder(context, CHANNEL_IDS),
        private val random: Random = Random()
) {

    private var notificationId: Int by bindSharedPreference(context, KEY_NOTIFICATION_ID, 0)

    fun sendBundledNotification(message: Message) =
            with(notificationManager) {
                channelBuilder.ensureChannelsExist(createChannel)
                randomChannelId.also {
                    notify(notificationId++, buildNotification(message, it))
                    notify(SUMMARY_ID, buildSummary(message, it))
                }
            }

    private val randomChannelId
        get() = CHANNEL_IDS[random.nextInt(CHANNEL_IDS.size)]

    @TargetApi(Build.VERSION_CODES.O)
    private val createChannel: (channelId: String) -> NotificationChannel? = { channelId ->
        when (channelId) {
            IMPORTANT_CHANNEL_ID -> NotificationChannel(channelId,
                    context.getString(R.string.important_channel_name),
                    NotificationManager.IMPORTANCE_HIGH).apply {
                description = context.getString(R.string.important_channel_description)
            }
            NORMAL_CHANNEL_ID -> NotificationChannel(channelId,
                    context.getString(R.string.normal_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = context.getString(R.string.normal_channel_description)
            }
            LOW_CHANNEL_ID -> NotificationChannel(channelId,
                    context.getString(R.string.low_channel_name),
                    NotificationManager.IMPORTANCE_LOW).apply {
                description = context.getString(R.string.low_channel_description)
            }
            else -> null
        }
    }

    private fun buildNotification(message: Message, channelId: String): Notification =
            with(NotificationCompat.Builder(context, channelId)) {
                message.apply {
                    setContentTitle(sender)
                    setContentText(text)
                    setWhen(timestamp.toEpochMilli())
                }
                setSmallIcon(getIconId(channelId))
                setShowWhen(true)
                setGroup(GROUP_KEY)
                setContentIntent(getContentIntent())
                build()
            }

    private fun getIconId(channelId: String) =
            when (channelId) {
                IMPORTANT_CHANNEL_ID -> R.drawable.ic_important
                LOW_CHANNEL_ID -> R.drawable.ic_low
                else -> R.drawable.ic_message
            }

    private fun getContentIntent(): PendingIntent =
            TaskStackBuilder.create(context).run {
                addParentStack(SecondActivity::class.java)
                addNextIntent(createIntent(SecondActivity::class.java))
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT) as PendingIntent
            }

    private fun createIntent(cls: Class<*>): Intent =
            Intent(context, cls).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            }

    private fun buildSummary(message: Message, channelId: String): Notification =
            with(NotificationCompat.Builder(context, channelId)) {
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
        private const val IMPORTANT_CHANNEL_ID = "IMPORTANT_CHANNEL_ID"
        private const val NORMAL_CHANNEL_ID = "NORMAL_CHANNEL_ID"
        private const val LOW_CHANNEL_ID = "LOW_CHANNEL_ID"
        private val CHANNEL_IDS =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    listOf(IMPORTANT_CHANNEL_ID, NORMAL_CHANNEL_ID, LOW_CHANNEL_ID)
                } else {
                    listOf(NORMAL_CHANNEL_ID)
                }
    }
}
