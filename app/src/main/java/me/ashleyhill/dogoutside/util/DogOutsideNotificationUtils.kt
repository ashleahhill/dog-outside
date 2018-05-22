package me.ashleyhill.dogoutside.util

import android.app.NotificationManager
import android.app.NotificationChannel
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import me.ashleyhill.dogoutside.MainActivity
import me.ashleyhill.dogoutside.R
import me.ashleyhill.dogoutside.data.DogOutsidePreferences


class DogOutsideNotificationUtils {

    companion object {
        private const val OUTSIDE_NOTIFICATION_ID: Int = 1000
        private const val OUTSIDE_NOTIFICATION_CHANNEL_ID: String = "dog-outside-timer"
        private var notificationBuilder: NotificationCompat.Builder? = null
        private var TAG: String = DogOutsideNotificationUtils::class.java.simpleName

        private fun createOutsideNotification(context: Context) {
            Log.d(TAG, "Create Notification")

            with(context) {
                createNotificationChannel(this)

                notificationBuilder = NotificationCompat.Builder(this, OUTSIDE_NOTIFICATION_CHANNEL_ID)
                        .setColor(getColor(R.color.colorPrimary))
                        .setContentTitle(DogOutsidePreferences.getDogNotification(context))
                        .setSmallIcon(R.drawable.ic_access_time_black_24dp)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setOngoing(true)
                        .setContentIntent(getOpenMainIntent(this))
                        .setOnlyAlertOnce(true)
            }
        }

        fun startService(context: Service) {
            if (notificationBuilder == null) {
                createOutsideNotification(context)
            }
            notificationBuilder!!.setContentText(DogOutsidePreferences.getTimeElapsedOutsideFormatted(context))
                context.startForeground(OUTSIDE_NOTIFICATION_ID, notificationBuilder!!.build())
        }

        fun notifyOutside(context: Context) {

            if (notificationBuilder == null) {
                createOutsideNotification(context)
            }

            notificationBuilder!!.setContentText(DogOutsidePreferences.getTimeElapsedOutsideFormatted(context))

            val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)
            notificationManager.notify(OUTSIDE_NOTIFICATION_ID, notificationBuilder!!.build())
        }

        fun cancelNotifyOutside(context: Context) {
            val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)

            notificationManager.cancel(OUTSIDE_NOTIFICATION_ID)
        }

        private fun getOpenMainIntent(context: Context): PendingIntent {
            val mainActivityIntent = Intent(context, MainActivity::class.java)

            return PendingIntent.getActivity(context, 0, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        private fun createNotificationChannel(context: Context) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = context.getString(R.string.notification_channel_title)
                val description = context.getString(R.string.notification_channel_description)
                val importance = NotificationManager.IMPORTANCE_HIGH

                val channel = NotificationChannel(OUTSIDE_NOTIFICATION_CHANNEL_ID, name, importance)
                channel.description = description
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                val notificationManager = context.getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

}