package me.ashleyhill.dogoutside.sync

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import me.ashleyhill.dogoutside.util.DogOutsideNotificationUtils
import java.util.*
import java.util.concurrent.TimeUnit

private val TAG = OutsideTimerService::class.java.simpleName


class OutsideTimerService : Service() {
    companion object {
        private val INTERVAL = TimeUnit.SECONDS.toMillis(1)
        private var timer: Timer? = null
        private var timerActive = false
    }

    private val mBinder = LocalBinder()

    override fun onBind(intent: Intent): IBinder? {
        if (timer == null) {
            timer = Timer()
        }

        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        super.onUnbind(intent)
        return true
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "Create Service")
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
        Log.d(TAG, "Start Timer")
        if (!timerActive) {

            timer?.scheduleAtFixedRate(NotifyTask(), 0, INTERVAL)
            timerActive = true
        }
    }

    fun stopTimer() {
        Log.d(TAG, "Stop Timer")
        DogOutsideNotificationUtils.cancelNotifyOutside(this@OutsideTimerService.baseContext)
        killTimer()
        stopSelf()
    }

    private fun killTimer() {
        timerActive = false
        if (timer != null) {
            timer?.cancel()
            timer = Timer()
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "Start Service")


        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d(TAG, "Destroy Service")
        super.onDestroy()
    }
}