package me.ashleyhill.dogoutside

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.fragment_dog_photo_display.*
import me.ashleyhill.dogoutside.data.DogOutsidePreferences

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DogPhotoDisplay.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DogPhotoDisplay.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DogPhotoDisplay : Fragment(),
        SharedPreferences.OnSharedPreferenceChangeListener {

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
        var view = inflater.inflate(R.layout.fragment_dog_photo_display, container, false)
        var imageView = view.findViewById<ImageView>(R.id.dog_photo_display)

        setImageFromPreference(view, imageView)
        return view
    }

    private fun setImageFromPreference(view: View, imageView: ImageView) {
        val context = this.requireContext()
        val dogStatus = DogOutsidePreferences.getDogStatus(context)

        var drawableId: Int? = null

        when (dogStatus) {
            getString(R.string.pref_dog_status_outside) -> drawableId = R.drawable.dog_outside
            getString(R.string.pref_dog_status_inside) -> drawableId = R.drawable.dog_inside
            getString(R.string.pref_dog_status_bed) -> drawableId = R.drawable.dog_in_bed
        }

        GlideApp.with(view)
                .load(drawableId)
                .centerCrop()
                .into(imageView)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == getString(R.string.pref_dog_status_key)) {
           setImageFromPreference(this.view!!, this.dog_photo_display)
        }
    }
}
