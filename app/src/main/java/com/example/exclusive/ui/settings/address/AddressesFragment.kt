package com.example.exclusive.ui.settings.address

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.exclusive.R
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentAddressesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressesFragment : Fragment() {

    private var _binding: FragmentAddressesBinding? = null
    private val binding get() = _binding!!
    private val addressesViewModel: AddressesViewModel by viewModels()
    private lateinit var addressesAdapter: AddressesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addressesAdapter = AddressesAdapter()
        binding.rvAddresses.adapter = addressesAdapter

        setListeners()
        observeViewModel()

        addressesViewModel.fetchCustomerAddresses()
    }

    private fun setListeners() {
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_addressesFragment_to_addAddressFragment)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            addressesViewModel.addresses.collect { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        // Show loading state
                    }
                    is UiState.Success -> {
                        addressesAdapter.submitList(uiState.data)
                    }
                    is UiState.Error -> {
                        // Handle error state
                        Toast.makeText(requireContext(), "Error: ${uiState.exception.message}", Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Idle -> {
                        // Initial state, do nothing
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