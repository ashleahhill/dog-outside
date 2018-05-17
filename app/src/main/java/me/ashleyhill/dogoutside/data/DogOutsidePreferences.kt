package me.ashleyhill.dogoutside.data

import android.content.Context
import android.content.SharedPreferences
import me.ashleyhill.dogoutside.R
import android.preference.PreferenceManager
import android.util.Log


class DogOutsidePreferences {
    private val TAG = DogOutsidePreferences::class.simpleName

    fun getDogName(context: Context, sharedPreferences: SharedPreferences): String {
        val dogName = sharedPreferences.getString(context.getString(R.string.pref_dog_name_key), "")
       return context.getString(R.string.dog_title, dogName)
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

    private fun log(string: String) {
        Log.d(TAG, string)
    }
}