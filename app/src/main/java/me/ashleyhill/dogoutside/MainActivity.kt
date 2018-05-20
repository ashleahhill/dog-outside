package me.ashleyhill.dogoutside

import android.content.Context
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
import android.content.ComponentName
import me.ashleyhill.dogoutside.sync.OutsideTimerService.LocalBinder
import android.os.IBinder
import android.content.ServiceConnection


class MainActivity :
        AppCompatActivity(),
        MainContent.OnFragmentInteractionListener,
        DogPhotoDisplay.OnFragmentInteractionListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private val TAG = MainActivity::class.java.simpleName
    private var mService: OutsideTimerService? = null
    private var mBound: Boolean = false

    override fun onFragmentInteraction(uri: Uri) {
        Log.d(TAG, uri.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, OutsideTimerService::class.java)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        unbindService(mConnection)
        mBound = false
    }
    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this)
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

    private fun handleDogOutside() {
        DogOutsidePreferences.setTimeOutsideStart(this)

        if (mBound) {
            mService?.startTimer()
        }
    }

    private fun handleDogInside() {
        DogOutsidePreferences.clearTimeOutsideStart(this)

        if (mBound) {
            mService?.stopTimer()
        }
    }

    private fun handleDogStatus() {
        if (DogOutsidePreferences.getDogOutside(this)) {
            handleDogOutside()
        } else {
            handleDogInside()
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == this.getString(R.string.pref_dog_status_key)) {
            handleDogStatus()
        }
    }

    /** Defines callbacks for service binding, passed to bindService()  */
    private val mConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName,
                                        service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as LocalBinder
            mService = binder.service
            mBound = true

            handleDogStatus()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }
}
