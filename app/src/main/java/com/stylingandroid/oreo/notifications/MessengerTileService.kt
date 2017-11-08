package com.stylingandroid.oreo.notifications

import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build
import android.service.quicksettings.Tile.STATE_ACTIVE
import android.service.quicksettings.Tile.STATE_INACTIVE
import android.service.quicksettings.TileService
import com.stylingandroid.oreo.notifications.messenger.ServiceScheduler

@TargetApi(Build.VERSION_CODES.N)
class MessengerTileService : TileService() {
    private val serviceScheduler: ServiceScheduler = ServiceScheduler(this)

    override fun onStartListening() {
        (if (serviceScheduler.isEnabled) STATE_ACTIVE else STATE_INACTIVE).apply {
            updateTileState(this)
        }
    }

    private fun updateTileState(state: Int) =
            qsTile?.apply {
                setState(state)
                with(icon) {
                    setTint(when (state) {
                        STATE_ACTIVE -> Color.WHITE
                        else -> Color.GRAY
                    })
                }
                updateTile()
            }

    override fun onClick() {
        super.onClick()
        qsTile?.apply {
            when (state) {
                STATE_INACTIVE -> {
                    serviceScheduler.startService()
                    updateTileState(STATE_ACTIVE)
                    println("Activated")
                }
                STATE_ACTIVE -> {
                    serviceScheduler.stopService()
                    updateTileState(STATE_INACTIVE)
                    println("Deactivated")
                }
                else -> updateTileState(STATE_INACTIVE)
            }
        }
    }
}
