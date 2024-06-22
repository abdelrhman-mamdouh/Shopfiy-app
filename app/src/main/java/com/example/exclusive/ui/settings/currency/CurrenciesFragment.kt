package com.example.exclusive.ui.settings.currency

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exclusive.R
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentCurrenciesBinding
import com.example.exclusive.ui.settings.currency.CurrenciesAdapter
import com.example.exclusive.utilities.SnackbarUtils
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
        setupSearchView()
        return binding.root
    }

    private fun setupAdapter() {
        adapter = CurrenciesAdapter(currencies, CurrenciesAdapter.ClickListener(viewModel::fetchCurrencies))
        binding.rvCurrency.setHasFixedSize(true)
        binding.rvCurrency.adapter = adapter


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    parentFragmentManager.popBackStack()
                    findNavController().navigate(R.id.action_currenciesFragment_to_settingsFragment)
                }
            })
        binding.titleBar.icBack.setOnClickListener {
            parentFragmentManager.popBackStack()
            findNavController().navigate(R.id.action_currenciesFragment_to_settingsFragment)
        }
    }

    private fun setupSearchView() {
        binding.svCurrency.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                val searchText = s.toString().trim()
                adapter.filter.filter(searchText)
            }
        })
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleBar.tvTitle.text = getString(R.string.currency)
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
                            viewModel.saveCurrency(Pair(currencies.base, currencies.results["EGP"]!!))
                            SnackbarUtils.showSnackbar(requireContext(), requireView(), "Current Currency is: ${currencies.base}")
                            Log.d(TAG, "${currencies.base} ${currencies.results["EGP"]}")
                            Log.d("CurrenciesFragment", "Currencies: $currencies")
                        }
                        is UiState.Error -> {
                            Log.e(TAG, "Error fetching currencies", uiState.exception)
                        }
                        UiState.Loading -> {
                            Log.d(TAG, "Loading...")
                        }
                        UiState.Idle -> {}
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}