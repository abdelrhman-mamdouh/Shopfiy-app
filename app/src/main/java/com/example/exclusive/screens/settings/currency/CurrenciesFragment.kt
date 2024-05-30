package com.example.exclusive.screens.settings.currency

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentCurrenciesBinding
import com.example.exclusive.utilities.currencies
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrenciesFragment : Fragment() {

    private var _binding: FragmentCurrenciesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CurrenciesAdapter

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
        setupAdapter()
        return binding.root
    }

    private fun setupAdapter() {
        Log.d(TAG, "setupAdapter")
        binding.rvCurrency.layoutManager = LinearLayoutManager(requireContext())
        adapter = CurrenciesAdapter(currencies, CurrenciesAdapter.ClickListener(viewModel::fetchCurrencies))
        binding.rvCurrency.setHasFixedSize(true)
        binding.rvCurrency.adapter = adapter
        adapter.updateCurrencies(currencies)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCurrencies()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}