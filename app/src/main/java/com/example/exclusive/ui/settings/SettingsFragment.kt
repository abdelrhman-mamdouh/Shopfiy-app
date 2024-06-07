package com.example.exclusive.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.exclusive.HolderActivity
import com.example.exclusive.R
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "SettingsFragment"
@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        binding.titleBar.tvTitle.text = getString(R.string.settings)

        lifecycleScope.launch {
            viewModel.currenciesStateFlow.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        // Handle the success state
                        val currency = uiState.data
                        // Update the UI with the currency data
                        binding.tvCode.text = "${currency.first}: ${currency.second}"
                    }
                    is UiState.Error -> {
                        // Handle the error state
                        // Show error message
                    }
                    is UiState.Loading -> {
                        // Handle the loading state
                    }
                    UiState.Idle -> {
                        // Handle the idle state if necessary
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Fetch the currencies when the fragment is resumed
        viewModel.fetchCurrencies()
    }

    private fun setListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })

        binding.titleBar.icBack.setOnClickListener{
            requireActivity().finish()
        }
        binding.tvAddress.setOnClickListener {
            val intent = Intent(requireContext(), HolderActivity::class.java).apply {
                putExtra(HolderActivity.GO_TO, "ADDRESS")
            }
            startActivity(intent)
        }

        binding.cvCurrency.setOnClickListener {
            val intent = Intent(requireContext(), HolderActivity::class.java).apply {
                putExtra(HolderActivity.GO_TO, "CURRENCY")
            }
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}