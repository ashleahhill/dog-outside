package me.ashleyhill.dogoutside.util

import android.content.Context
import android.content.Intent
import android.support.annotation.IntDef
import junit.framework.Assert
import me.ashleyhill.dogoutside.sync.OutsideTimerService
import me.ashleyhill.dogoutside.util.OutsideTimerIntentBuilder.Companion.KEY_COMMAND
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

// https://proandroiddev.com/deep-dive-into-android-services-4830b8c9a09




@IntDef(OutsideTimerIntentBuilder.INVALID, OutsideTimerIntentBuilder.STOP, OutsideTimerIntentBuilder.START)
@Retention(RetentionPolicy.SOURCE)
internal annotation class Command

class OutsideTimerIntentBuilder(context: Context) {
    companion object {
        private val KEY_MESSAGE = "msg"
        private val KEY_COMMAND = "cmd"
        const val INVALID = -1
        const val STOP = 0
        const val START = 1

        fun containsCommand(intent: Intent): Boolean {
            return intent.extras!!.containsKey(KEY_COMMAND)
        }

        fun containsMessage(intent: Intent): Boolean {
            return intent.extras!!.containsKey(KEY_MESSAGE)
        }

        @Command
        fun getCommand(intent: Intent): Int {
            return intent.extras!!.getInt(KEY_COMMAND)
        }

        fun getMessage(intent: Intent): String {
            return intent.extras!!.getString(KEY_MESSAGE)
        }
    }

    var mContext: Context? = null
    var mMessage: String? = null
    var mCommandId: Int? = null

    init {
        this.mContext = context
    }

    fun setMessage(message: String): OutsideTimerIntentBuilder {
        this.mMessage = message
        return this
    }

    fun setCommand(@Command command: Int): OutsideTimerIntentBuilder {
        this.mCommandId = command
        return this
    }

    fun build(): Intent {
        Assert.assertNotNull("Context can not be null!", mContext)
        val intent = Intent(mContext, OutsideTimerService::class.java)
        if (mCommandId !== INVALID) {
            intent.putExtra(KEY_COMMAND, mCommandId)
        }
        if (mMessage != null) {
            intent.putExtra(KEY_MESSAGE, mMessage)
        }
        return intent
    }


}