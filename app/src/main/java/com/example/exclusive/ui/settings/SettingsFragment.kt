package com.example.exclusive.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exclusive.HolderActivity
import com.example.exclusive.R
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentSettingsBinding
import com.example.exclusive.model.ProductNode
import com.example.exclusive.ui.productinfo.DailogFramgent
import com.example.exclusive.ui.watchlist.WatchListAdapter
import com.example.exclusive.ui.watchlist.WatchlistFragmentDirections
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

        val adapter = WatchListAdapter({ product ->

            val dialog = DailogFramgent(
                onDialogPositiveClick = {
                    viewModel.removeProductFromWatchList(product.id.substring(22))
                },
                onDialogNegativeClick = {

                }
            )
            dialog.show(requireActivity().supportFragmentManager, "dialog")
        }, { product: ProductNode ->
            NavHostFragment.findNavController(this@SettingsFragment).navigate(
                WatchlistFragmentDirections.actionWatchlistFragmentToProductInfoFragment(product))
        }, addToCart = {product: ProductNode ->
            viewModel.addToCart(product.variants.edges[0].node.title, 1)
        })

        binding.rvWishlist.adapter = adapter
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.watchlist.collect{
                    adapter.submitList(it)
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