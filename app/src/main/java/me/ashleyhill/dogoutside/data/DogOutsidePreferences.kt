package me.ashleyhill.dogoutside.data

import android.content.Context
import android.content.SharedPreferences
import me.ashleyhill.dogoutside.R
import android.preference.PreferenceManager



class DogOutsidePreferences {
    fun getDogName(context: Context, sharedPreferences: SharedPreferences): String {
             val dogName = sharedPreferences.getString(context.getString(R.string.pref_dog_name_key), "")
       return context.getString(R.string.dog_title, dogName)
    }
}