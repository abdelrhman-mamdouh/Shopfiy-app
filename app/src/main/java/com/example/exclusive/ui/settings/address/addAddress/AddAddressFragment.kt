package com.example.exclusive.ui.settings.address.addAddress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.exclusive.AddAddressToCustomerMutation
import com.example.exclusive.R
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentAddAddressBinding
import com.example.exclusive.model.AddressInput
import com.example.exclusive.utilities.SnackbarUtils
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
        binding.titleBar.tvTitle.text = getString(R.string.add_address)
        binding.titleBar.icBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
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
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is UiState.Success -> {

                        Toast.makeText(requireContext(), "Here ${uiState.data}", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                        if (uiState.data) {
                            SnackbarUtils.showSnackbar(requireContext(),view,"Address added successfully")
                            findNavController().navigateUp()
                        }
                    }
                    is UiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        SnackbarUtils.showSnackbar(requireContext(),view,"Error adding address: ${uiState.exception.message}")

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