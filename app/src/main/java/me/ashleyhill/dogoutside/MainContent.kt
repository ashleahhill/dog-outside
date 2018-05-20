package me.ashleyhill.dogoutside

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_main_content.*
import me.ashleyhill.dogoutside.data.DogOutsidePreferences

private val TAG: String? = MainContent::class.simpleName

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MainContent.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MainContent.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MainContent : Fragment(),
        SharedPreferences.OnSharedPreferenceChangeListener,
        AdapterView.OnItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
            sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this.context)
                .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_content, container, false)
        val context = this.requireContext()

        // Set the dog title "Snowy is..."
        val dogTitle = DogOutsidePreferences.getDogTitle(context)
        view.findViewById<TextView>(R.id.tv_dog_title).text = dogTitle

        // Sync the dog status spinner with what's in preferences
        val spinnerDogStatus = view.findViewById<Spinner>(R.id.spinner_dog_status)
        spinnerDogStatus.setSelection((spinnerDogStatus.adapter as ArrayAdapter<String>).getPosition(DogOutsidePreferences.getDogStatus(context)))

        // set this as the spinner listener
        spinnerDogStatus.onItemSelectedListener = this

        return view
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == getString(R.string.pref_dog_name_key)) {
           this.tv_dog_title.text = DogOutsidePreferences.getDogTitle(this.requireContext());
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val value = parent.getItemAtPosition(position)

        Log.i(TAG, value.toString())

        val context = this.requireContext()

        DogOutsidePreferences.setDogStatus(context, value.toString())
    }
}
