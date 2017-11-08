package com.stylingandroid.oreo.notifications.messenger

import android.content.Context
import com.google.android.gms.gcm.GcmNetworkManager
import com.google.android.gms.gcm.OneoffTask
import com.google.android.gms.gcm.Task
import com.stylingandroid.oreo.notifications.bindSharedPreference
import com.stylingandroid.oreo.notifications.lazyFast
import com.stylingandroid.oreo.notifications.safeContext
import org.threeten.bp.Duration
import java.util.*

class ServiceScheduler(
        context: Context,
        private val random: Random = Random()) {

    private val safeContext: Context by lazyFast { context.safeContext() }

    private val networkManager: GcmNetworkManager by lazyFast {
        GcmNetworkManager.getInstance(safeContext)
    }
    var isEnabled: Boolean by bindSharedPreference(context, KEY_ENABLED, true)

    fun startService() {
        isEnabled = true
        scheduleService()
    }

    fun scheduleService() =
            with(OneoffTask.Builder()) {
                setRequiredNetwork(Task.NETWORK_STATE_ANY)
                setRequiresCharging(false)
                setService(MessengerService::class.java)
                ((random.nextInt(WINDOW_MAX_OFFSET) + 1) * Duration.ofMinutes(1L).seconds).also {
                    setExecutionWindow(it, it + WINDOW_SIZE_SECONDS)
                    println("Scheduled between $it and ${it + WINDOW_SIZE_SECONDS}")
                }
                setUpdateCurrent(false)
                setTag(TAG)
                networkManager.schedule(build())
            }


    fun stopService() {
        networkManager.cancelTask(TAG, MessengerService::class.java)
        isEnabled = false
    }

    companion object {
        private val TAG: String = ServiceScheduler::class.java.canonicalName
        private const val WINDOW_MAX_OFFSET = 1
        private const val WINDOW_SIZE_MINUTES = 1L
        private val WINDOW_SIZE_SECONDS = Duration.ofMinutes(WINDOW_SIZE_MINUTES).seconds
        private const val KEY_ENABLED = "KEY_ENABLED"
    }
}
