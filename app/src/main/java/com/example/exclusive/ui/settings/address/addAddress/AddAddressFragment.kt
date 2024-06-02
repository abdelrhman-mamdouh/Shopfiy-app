package com.example.exclusive.ui.settings.address.addAddress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.exclusive.AddAddressToCustomerMutation
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentAddAddressBinding
import com.example.exclusive.model.AddressInput
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "AddAddressFragment"
@AndroidEntryPoint
class AddAddressFragment : Fragment() {

    private var _binding: FragmentAddAddressBinding? = null
    private val binding get() = _binding!!
    private val addAddressViewModel: AddAddressViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val details = binding.etDetails.text.toString()
            val phone = binding.tvPhone.text.toString()
            val city = binding.etCity.text.toString()
            val country = binding.etCountry.text.toString()
            val zip = binding.etZip.text.toString()
            val isDefault = binding.cbDefaultAddress.isChecked

            val address = AddressInput(
                firstName = title,
                address1 = details,
                phone = phone,
                city = city,
                country = country,
                zip = zip
            )
            val mailingAddressInput = address.toInput()
            addAddressViewModel.addAddress(mailingAddressInput)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            addAddressViewModel.addAddressResult.collect { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        // Show loading indicator
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is UiState.Success -> {
                        // Hide loading indicator
                        Toast.makeText(requireContext(), "Here ${uiState.data}", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                        if (uiState.data) {
                            // Address added successfully, navigate back or show a success message
                            Toast.makeText(requireContext(), "Address added successfully", Toast.LENGTH_SHORT).show()
                            findNavController().navigateUp()
                        }
                    }
                    is UiState.Error -> {
                        // Hide loading indicator and show error message
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Error adding address: ${uiState.exception.message}", Toast.LENGTH_LONG).show()
                    }
                    is UiState.Idle -> {
                        // Initial idle state, do nothing
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}