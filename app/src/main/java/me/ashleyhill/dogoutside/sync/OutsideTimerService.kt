package me.ashleyhill.dogoutside.sync

import android.content.Intent
import android.os.IBinder
import android.R.string.cancel
import android.app.IntentService
import android.app.Service
import android.content.Context
import android.os.Binder
import android.support.annotation.Nullable
import android.util.Log
import java.util.Timer;
import java.util.TimerTask;
import android.widget.Toast
import me.ashleyhill.dogoutside.util.DogOutsideNotificationUtils
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask


/**
 * https://medium.com/@raziaranisandhu/create-services-never-stop-in-android-b5dcfc5fb4b2
 */
class OutsideTimerService : Service() {
    companion object {
        private val TAG = "OutsideTimerService"
        private val INTERVAL = TimeUnit.SECONDS.toMillis(1)
    }

    private var timer: Timer? = null
    private val mBinder = LocalBinder()

    override fun onBind(intent: Intent): IBinder? {

        return mBinder
    }

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "Start Service")
    }

    inner class LocalBinder: Binder() {
         val service: OutsideTimerService get() = this@OutsideTimerService
    }

    inner class NotifyTask: TimerTask() {
        override fun run() {
            // Print a log
            Log.d(TAG, "Running")
            DogOutsideNotificationUtils.notifyOutside(this@OutsideTimerService.baseContext)
        }
    }

    fun startTimer() {
        if (timer == null) {
            timer = Timer()
        }
        timer?.scheduleAtFixedRate(NotifyTask(), 0, INTERVAL)
    }

    fun stopTimer() {
        DogOutsideNotificationUtils.cancelNotifyOutside(this@OutsideTimerService.baseContext)
        timer?.cancel()
        timer = null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "Start to do an action")

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d(TAG, "StopService")

        super.onDestroy()
    }
}