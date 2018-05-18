package me.ashleyhill.dogoutside.util

import android.app.NotificationManager
import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import me.ashleyhill.dogoutside.R


class DogOutsideNotificationUtils {
    private val OUTSIDE_NOTIFICATION_ID: Int = 1000
    private val OUTSIDE_NOTIFICATION_CHANNEL_ID: String = "dog-outside-timer"
    private var notificationBuilder: NotificationCompat.Builder? = null

    private fun createOutsideNotification(context: Context) {

        with(context) {
            createNotificationChannel(this)

            notificationBuilder = NotificationCompat.Builder(this, OUTSIDE_NOTIFICATION_CHANNEL_ID)
                    .setColor(getColor(R.color.colorPrimary))
                    .setContentText(getString(R.string.notification_dog_outside))
                    .setSmallIcon(R.drawable.ic_access_time_black_24dp)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setOngoing(true)
                    .setOnlyAlertOnce(true)
        }
    }

    fun notifyOutside(context: Context) {
        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)

        if (notificationBuilder == null) {
            createOutsideNotification(context)
        }
        notificationBuilder!!.setContentTitle(context.getString(R.string.notification_dog_outside_title))

        notificationManager.notify(OUTSIDE_NOTIFICATION_ID, notificationBuilder!!.build())
    }

    fun cancelNotifyOutside(context: Context) {
        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)

        notificationManager.cancel(OUTSIDE_NOTIFICATION_ID);
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