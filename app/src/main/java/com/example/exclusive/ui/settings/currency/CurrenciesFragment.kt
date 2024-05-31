package com.example.exclusive.ui.settings.currency

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentCurrenciesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrenciesFragment : Fragment() {

    private var _binding: FragmentCurrenciesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CurrenciesViewModel by viewModels()

    companion object {
        private const val TAG = "CurrenciesFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrenciesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCurrencies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeCurrencies() {
        lifecycleScope.launch {
            viewModel.currenciesStateFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { uiState ->
                    when (uiState) {
                        is UiState.Success -> {
                            val currencies = uiState.data
                            Log.d("CurrenciesFragment", "Currencies: $currencies")
                        }
                        is UiState.Error -> {
                            Log.e("CurrenciesFragment", "Error fetching currencies", uiState.exception)
                        }
                        UiState.Loading -> {
                            Log.d("CurrenciesFragment", "Loading...")
                        }
                        UiState.Idle -> {

                        }
                    }
                }
        }
    }
}