package me.ashleyhill.dogoutside.sync

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import me.ashleyhill.dogoutside.util.DogOutsideNotificationUtils
import java.util.*
import java.util.concurrent.TimeUnit
import android.os.Build
import android.annotation.TargetApi
import android.content.Context
import android.support.annotation.IntDef
import junit.framework.Assert
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import android.os.Build.VERSION_CODES.O
import me.ashleyhill.dogoutside.util.OutsideTimerIntentBuilder
import java.util.concurrent.Executors


private val TAG = OutsideTimerService::class.java.simpleName


// Command enumeration
// more info - http://blog.shamanland.com/2016/02/int-string-enum.html

private const val INVALID = -1
private const val STOP = 0
private const val START = 1
@IntDef(INVALID, STOP, START)
@Retention(RetentionPolicy.SOURCE)
internal annotation class Command

class OutsideTimerService : Service() {
    companion object {
        private val INTERVAL = TimeUnit.SECONDS.toMillis(1)
        private var mTimer: Timer? = null
        private var timerActive = false
        private var mTimeRunning_sec = 0
    }

    private var mBinder: IBinder? = null

    override fun onBind(intent: Intent): IBinder? {
        if (mBinder == null) {
            mBinder = LocalBinder()
        }
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        super.onUnbind(intent)
        return false
    }

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "Create Service")
    }

    override fun onStartCommand(
            intent: Intent, flags: Int, startId: Int): Int {

        Log.d(TAG, "Start Service")
        val containsCommand = OutsideTimerIntentBuilder
                .containsCommand(intent)
        Log.d(TAG,
                String.format(
                        "Service in [%s] state. id: [%d]. startId: [%d]",
                        if (mServiceIsStarted) "STARTED" else "NOT STARTED",
                        if (containsCommand)
                            OutsideTimerIntentBuilder.getCommand(intent)
                        else
                            "N/A",
                        startId))
        mServiceIsStarted = true
        routeIntentToCommand(intent)

//        return super.onStartCommand(intent, flags, startId)

        return Service.START_NOT_STICKY
    }

    inner class LocalBinder: Binder() {
         val service: OutsideTimerService get() = this@OutsideTimerService
    }

    inner class NotifyTask: TimerTask() {
        override fun run() {

            // Print a log
            Log.d(TAG, "Running")
            DogOutsideNotificationUtils.notifyOutside(this@OutsideTimerService)
        }
    }

    fun startTimer() {
        Log.d(TAG, "Start Timer")

        mTimer?.scheduleAtFixedRate(NotifyTask(), 0, INTERVAL)
        timerActive = true
    }

    fun stopTimer() {
        Log.d(TAG, "Stop Timer")
        killTimer()
        DogOutsideNotificationUtils.cancelNotifyOutside(this@OutsideTimerService)
    }

    private fun killTimer() {
        timerActive = false
        if (mTimer != null) {
            mTimer?.cancel()
        }
        mTimer = null
    }

    var mServiceIsStarted: Boolean = false

    private fun commandStart() {

        if (!mServiceIsStarted) {
            moveToStartedState()
            return
        }

        if (mTimer == null) {
            mTimeRunning_sec = 0

            DogOutsideNotificationUtils.startService(this)

            mTimer = Timer()
            startTimer()


            Log.d(TAG, "commandStart: starting executor")
        } else {
            Log.d(TAG, "commandStart: do nothing")
        }
    }

    private fun stopCommand() {
        stopTimer()
        stopForeground(true)
        stopSelf()
    }

    private fun processCommand(@Command command: Int) {
        when(command) {
            OutsideTimerIntentBuilder.START -> commandStart()
            OutsideTimerIntentBuilder.STOP -> stopCommand()
            else -> Log.d(TAG, "Invalid Command" + command.toString())
        }
    }

    private fun processMessage(message: String) {
        Log.d(TAG, "Message: " + message)
    }

    private fun routeIntentToCommand(intent: Intent?) {
        if (intent != null) {

            // process command
            if (OutsideTimerIntentBuilder.containsCommand(intent)) {
                processCommand(OutsideTimerIntentBuilder.getCommand(intent))
            }

            // process message
            if (OutsideTimerIntentBuilder.containsMessage(intent)) {
                processMessage(OutsideTimerIntentBuilder.getMessage(intent))
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    private fun moveToStartedState() {
        val intent = OutsideTimerIntentBuilder(this).setCommand(OutsideTimerIntentBuilder.START).build()

        if (isPreAndroidO()) {
            Log.d(TAG, "moveToStartedState: on N/lower")
            startService(intent)
        } else {
            Log.d(TAG, "moveToStartedState: on O")
            startForegroundService(intent)
        }
    }

    private fun isPreAndroidO(): Boolean {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O
    }


    override fun onDestroy() {
        Log.d(TAG, "Destroy Service")
        super.onDestroy()
    }



}