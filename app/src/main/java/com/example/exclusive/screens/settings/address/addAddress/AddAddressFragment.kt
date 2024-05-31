package com.example.exclusive.screens.settings.address.addAddress

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.exclusive.R
import com.example.exclusive.databinding.FragmentAddAddressBinding
import com.example.exclusive.utilities.Constants
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.gms.common.api.Status

private const val TAG = "AddAddressFragment"
class AddAddressFragment: Fragment() {
    private var _binding: FragmentAddAddressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAddressBinding.inflate(inflater, container, false)
        Places.initialize(requireContext().applicationContext, Constants.GOOGLE_KEY)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fAutoComplete = childFragmentManager.findFragmentById(R.id.f_auto_complete)
                as AutocompleteSupportFragment

        fAutoComplete.setPlaceFields(listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS_COMPONENTS
        ))

        fAutoComplete.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(status: Status) {
                Log.i(TAG, "An error occurred: $status")
            }

            override fun onPlaceSelected(place: Place) {
                Log.i(TAG, "Place: ${place.name}, ${place.id}")

                val addressComponents = place.addressComponents

                var city: String? = null
                var country: String? = null
                if (addressComponents != null) {
                    for (component in addressComponents.asList()) {
                        for (type in component.types) {
                            when (type) {
                                "locality" -> city = component.name
                                "country" -> country = component.name
                            }
                        }
                    }
                }

                binding.tvAddressTitle.text = place.address
                binding.tvCity.text = city
                binding.tvCountry.text = country

                val latLng = place.latLng
                if (latLng != null) {
                    Log.d(TAG, "onPlaceSelected: $latLng")
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}