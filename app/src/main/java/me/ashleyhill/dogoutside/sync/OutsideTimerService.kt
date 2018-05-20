package me.ashleyhill.dogoutside.sync

import android.content.Intent
import android.os.IBinder
import android.R.string.cancel
import android.app.IntentService
import android.app.Service
import android.content.Context
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

    private var timer = Timer()
    private var context: Context? = null


    override fun onBind(intent: Intent): IBinder? {
        // TODO Auto-generated method stub
        return null
    }

    override fun onCreate() {
        // TODO Auto-generated method stub
        super.onCreate()

        Log.d(TAG, "Start to do an action")
    }

    inner class NotifyTask: TimerTask() {
        override fun run() {
            // Print a log
            Log.d(TAG, "Running")
            DogOutsideNotificationUtils.notifyOutside(context!!)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "Start to do an action")

        context = this.applicationContext
        timer.scheduleAtFixedRate(NotifyTask(), 0, INTERVAL)
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onDestroy() {
        // Display the Toast Message
        Log.d(TAG, "StopService")
        if (context != null) {
            DogOutsideNotificationUtils.cancelNotifyOutside(context!!)
        }
        timer?.cancel()
        super.onDestroy()
    }
}