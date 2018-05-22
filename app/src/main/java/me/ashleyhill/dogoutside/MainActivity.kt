package me.ashleyhill.dogoutside

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import me.ashleyhill.dogoutside.data.DogOutsidePreferences
import me.ashleyhill.dogoutside.sync.OutsideTimerService
import me.ashleyhill.dogoutside.sync.OutsideTimerService.LocalBinder
import me.ashleyhill.dogoutside.util.OutsideTimerIntentBuilder

private val TAG = MainActivity::class.java.simpleName

class MainActivity :
        AppCompatActivity(),
        SharedPreferences.OnSharedPreferenceChangeListener {

    private var mService: OutsideTimerService? = null
    private var mBound: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStart() {
        super.onStart()
        bindTimer()
    }

    override fun onStop() {
        super.onStop()
        unbindTimer()
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

    private fun bindTimer() {
        val intent = OutsideTimerIntentBuilder(this).build()

        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindTimer() {
        unbindService(mConnection)
    }

    private fun handleDogOutside(fromUpdate: Boolean?) {
        Log.d(TAG, "Handle Dog Outside")
        DogOutsidePreferences.setTimeOutsideStart(this, fromUpdate)

        if (mBound) {
            Log.d(TAG, "service bound")
            startService(OutsideTimerIntentBuilder(this).setCommand(OutsideTimerIntentBuilder.START).build())
        } else {
            Log.d(TAG, "service not bound")

        }
    }

    private fun handleDogInside() {
        Log.d(TAG, "handleDogInside")
        DogOutsidePreferences.clearTimeOutsideStart(this)

        if (mBound) {
            startService(OutsideTimerIntentBuilder(this).setCommand(OutsideTimerIntentBuilder.STOP).build())
        } else {
            Log.d(TAG, "service not bound")
        }
    }

    private fun handleDogStatus(fromUpdate: Boolean?) {
        Log.d(TAG, "Handle Dog Status")

        if (DogOutsidePreferences.getDogOutside(this)) {
            handleDogOutside(fromUpdate)
        } else {
            handleDogInside()
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == this.getString(R.string.pref_dog_status_key)) {
            handleDogStatus(true)
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
            handleDogStatus(false)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }
}
