package me.ashleyhill.dogoutside.data

import android.content.Context
import android.content.SharedPreferences
import me.ashleyhill.dogoutside.R
import android.preference.PreferenceManager
import android.util.Log
import android.util.TimeUtils


class DogOutsidePreferences {
    private val TAG = DogOutsidePreferences::class.simpleName

    fun getDogTitle(context: Context, sharedPreferences: SharedPreferences): String {
        val dogName = sharedPreferences.getString(context.getString(R.string.pref_dog_name_key), "")
       return context.getString(R.string.dog_title, dogName)
    }

    fun getDogNotification(context: Context, sharedPreferences: SharedPreferences): String {
        val dogName = sharedPreferences.getString(context.getString(R.string.pref_dog_name_key), "")
       return context.getString(R.string.notification_dog_outside, dogName)
    }

    fun setDogStatus(context: Context, sharedPreferences: SharedPreferences, string: String) {
        log(string);
        with(sharedPreferences.edit()) {
            putString(context.getString(R.string.pref_dog_status_key), string)
            commit()
        }
    }

    fun getDogStatus(context: Context, sharedPreferences: SharedPreferences): String {
        return sharedPreferences.getString(context.getString(R.string.pref_dog_status_key), context.getString(R.string.pref_dog_status_default))
    }

    fun setTimeOutsideStart(context: Context, sharedPreferences: SharedPreferences) {
        log("Time start")
        with(sharedPreferences.edit()) {
            putLong(context.getString(R.string.pref_dog_outside_start_key), System.currentTimeMillis())
            commit()
        }
    }
    fun clearTimeOutsideStart(context: Context, sharedPreferences: SharedPreferences) {
        log("Time start")
        with(sharedPreferences.edit()) {
            putLong(context.getString(R.string.pref_dog_outside_start_key), 0)
            commit()
        }
    }

    fun getTimeOutsideStart(context: Context, sharedPreferences: SharedPreferences): Long {
        return sharedPreferences.getLong(context.getString(R.string.pref_dog_outside_start_key), 0)
    }

    fun getElapsedTimeOutside(context: Context, sharedPreferences: SharedPreferences): Long {

        var timeOutsideStart = getTimeOutsideStart(context, sharedPreferences)

        if (timeOutsideStart > 0 && System.currentTimeMillis() > timeOutsideStart) {
            return System.currentTimeMillis() - timeOutsideStart
        }

        return 0
    }

    fun getTimeElapsedOutsideFormatted(context: Context, sharedPreferences: SharedPreferences): String {
        var elapsedTimeOutside = getElapsedTimeOutside(context, sharedPreferences)

        if (elapsedTimeOutside > 0) {
            var totalMinutes = elapsedTimeOutside / 1000
            var minutes = totalMinutes / 60
            var seconds = totalMinutes % 60

            return String.format("%d:%02d", minutes, seconds)
        }
        return ""
    }


    private fun log(string: String) {
        Log.d(TAG, string)
    }
}