package me.ashleyhill.dogoutside

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.MenuItem
import me.ashleyhill.dogoutside.data.DogOutsidePreferences
import me.ashleyhill.dogoutside.sync.OutsideTimerService
import me.ashleyhill.dogoutside.util.DogOutsideNotificationUtils


class MainActivity :
        AppCompatActivity(),
        MainContent.OnFragmentInteractionListener,
        DogPhotoDisplay.OnFragmentInteractionListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private val TAG = MainActivity::class.java.simpleName

    override fun onFragmentInteraction(uri: Uri) {
        Log.d(TAG, uri.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * This is where we inflate and set up the menu for this Activity.
     *
     * @param menu The options menu in which you place your items.
     *
     * @return You must return true for the menu to be displayed;
     *         if you return false it will not be shown.
     *
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        /* Use kotlin property accessor to get menu inflater */
        val inflater = menuInflater
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.main_menu, menu)
        /* Return true so that the menu is displayed in the Toolbar */
        return true
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu.
     *
     * @param item The menu item that was selected by the user
     *
     * @return true if you handle the menu click here, false otherwise
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.action_settings) {
            startActivity(Intent(this, SettingsActivity::class.java))
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun handleDogOutside() {
        DogOutsidePreferences().setTimeOutsideStart(this, PreferenceManager.getDefaultSharedPreferences(this))
        startService(Intent(this, OutsideTimerService::class.java))
    }

    fun handleDogInside() {
        DogOutsidePreferences().clearTimeOutsideStart(this, PreferenceManager.getDefaultSharedPreferences(this))
        stopService(Intent(this, OutsideTimerService::class.java))
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == this.getString(R.string.pref_dog_status_key)) {
            if (DogOutsidePreferences().getDogStatus(this, sharedPreferences!!) == getString(R.string.pref_dog_status_outside)) {
                handleDogOutside()
            } else {
                handleDogInside()
            }

        }
    }
}
