package com.stylingandroid.oreo.notifications.messenger

import android.content.Context
import com.stylingandroid.oreo.notifications.R
import com.stylingandroid.oreo.notifications.lazyFast
import org.threeten.bp.Instant
import java.util.*

data class Message(val text: String, val sender: String, val timestamp: Instant)

class Messenger(
        context: Context,
        private val random: Random = Random()) {

    private val phrases: Array<String> by lazyFast {
        context.resources.getStringArray(R.array.phrases)
    }

    fun generateNewMessage(): Message =
            Message(getRandomPhrase(), SENDER, Instant.now())

    private fun getRandomPhrase(): String =
            random.nextInt(phrases.size).let {
                phrases[it]
            }

    companion object {
        private const val SENDER = "Styling Android"
    }
}
