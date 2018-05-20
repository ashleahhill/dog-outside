package me.ashleyhill.dogoutside.data

import android.content.Context
import android.content.SharedPreferences
import me.ashleyhill.dogoutside.R
import android.preference.PreferenceManager
import android.util.Log

class DogOutsidePreferences {
    companion object {

        private val TAG = DogOutsidePreferences::class.simpleName

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }

        fun getDogTitle(context: Context): String {
            val dogName = getSharedPreferences(context).getString(context.getString(R.string.pref_dog_name_key), "")
           return context.getString(R.string.dog_title, dogName)
        }

        fun getDogNotification(context: Context): String {
            val dogName = getSharedPreferences(context).getString(context.getString(R.string.pref_dog_name_key), "")
           return context.getString(R.string.notification_dog_outside, dogName)
        }

        fun setDogStatus(context: Context, string: String) {
            log(string);
            with(getSharedPreferences(context).edit()) {
                putString(context.getString(R.string.pref_dog_status_key), string)
                commit()
            }
        }

        fun getDogStatus(context: Context): String {
            return getSharedPreferences(context).getString(context.getString(R.string.pref_dog_status_key), context.getString(R.string.pref_dog_status_default))
        }

        fun getDogOutside(context: Context): Boolean {
            return getDogStatus(context) == context.getString(R.string.pref_dog_status_outside)
        }

        fun setTimeOutsideStart(context: Context) {
            log("Time start")
            with(getSharedPreferences(context).edit()) {
                putLong(context.getString(R.string.pref_dog_outside_start_key), System.currentTimeMillis())
                commit()
            }
        }
        fun clearTimeOutsideStart(context: Context) {
            log("Time start")
            with(getSharedPreferences(context).edit()) {
                putLong(context.getString(R.string.pref_dog_outside_start_key), 0)
                commit()
            }
        }

        fun getTimeOutsideStart(context: Context): Long {
            return getSharedPreferences(context).getLong(context.getString(R.string.pref_dog_outside_start_key), 0)
        }

        fun getElapsedTimeOutside(context: Context): Long {

            var timeOutsideStart = getTimeOutsideStart(context)

            if (timeOutsideStart > 0 && System.currentTimeMillis() > timeOutsideStart) {
                return System.currentTimeMillis() - timeOutsideStart
            }

            return 0
        }

        fun getTimeElapsedOutsideFormatted(context: Context): String {
            var elapsedTimeOutside = getElapsedTimeOutside(context)

            if (elapsedTimeOutside > 0) {
                var totalMinutes = elapsedTimeOutside / 1000
                var minutes = totalMinutes / 60
                var seconds = totalMinutes % 60

                return String.format("%d:%02d", minutes, seconds)
            }
            return "00:00"
        }


        private fun log(string: String) {
            Log.d(TAG, string)
        }
    }

}