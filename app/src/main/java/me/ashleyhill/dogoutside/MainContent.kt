package me.ashleyhill.dogoutside

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
        AdapterView.OnItemSelectedListener{

    private val TAG: String? = MainContent::class.simpleName
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
            sharedPreferences.registerOnSharedPreferenceChangeListener(this)

//        handleTimerService(this.context!!, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this.context)
                .unregisterOnSharedPreferenceChangeListener(this)
//        handleTimerService(this.context!!, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_content, container, false)
        val context = this.requireContext()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        // Set the dog title "Snowy is..."
        val dogTitle = DogOutsidePreferences().getDogTitle(context, sharedPreferences)
        view.findViewById<TextView>(R.id.tv_dog_title).text = dogTitle

        // Sync the dog status spinner with what's in preferences
        val spinnerDogStatus = view.findViewById<Spinner>(R.id.spinner_dog_status)
        spinnerDogStatus.setSelection((spinnerDogStatus.adapter as ArrayAdapter<String>).getPosition(DogOutsidePreferences().getDogStatus(context, sharedPreferences)))

        // set this as the spinner listener
        spinnerDogStatus.onItemSelectedListener = this

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainContent.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                MainContent().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == getString(R.string.pref_dog_name_key)) {
//            val dogName = sharedPreferences.getString(getString(R.string.pref_dog_name_key), "")
//            val dogTitle = getString(R.string.dog_title, dogName)
           this.tv_dog_title.text = DogOutsidePreferences().getDogTitle(this.requireContext(), sharedPreferences);
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val value = parent.getItemAtPosition(position)

        Log.i(TAG, value.toString())

        val context = this.requireContext()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.requireContext())

        DogOutsidePreferences().setDogStatus(context, sharedPreferences, value.toString())
    }
}
