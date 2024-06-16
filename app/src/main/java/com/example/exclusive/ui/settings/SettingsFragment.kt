package com.example.exclusive.ui.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.exclusive.R

import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentSettingsBinding
import com.example.exclusive.model.MyOrder
import com.example.exclusive.model.ProductNode
import com.example.exclusive.ui.orders.view.OnOrderClickListener
import com.example.exclusive.ui.orders.view.OrderAdapter
import com.example.exclusive.ui.productinfo.DailogFramgent
import com.example.exclusive.ui.watchlist.WatchListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "SettingsFragment"

@AndroidEntryPoint

class SettingsFragment : Fragment(), OnOrderClickListener {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isGuest.collect {
                    if (it) {
                        binding.cvOrder.visibility = View.GONE
                        binding.rvOrders.visibility = View.GONE
                        binding.cvWish.visibility = View.GONE
                        binding.cvAddress.visibility = View.GONE
                        binding.rvWishlist.visibility = View.GONE
                        binding.outlinedButton.text = "Login"
                    }
                }
            }
        }
        setListeners()
        binding.outlinedButton.setOnClickListener {
            if (binding.outlinedButton.text == "Login") {
                findNavController().navigate(
                    R.id.action_settingsFragment_to_loginFragment)
                return@setOnClickListener
            }
            viewModel.clearEmailAndToken()

            findNavController().navigate(
                R.id.action_settingsFragment_to_loginFragment)

            requireActivity().finish()
        }
        lifecycleScope.launch {
            viewModel.currenciesStateFlow.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        hideLoading()
                        Log.d(TAG, "onViewCreated: s")
                        val currency = uiState.data
                        binding.tvCode.text = "${currency.first}: ${currency.second}"
                    }

                    is UiState.Error -> {
                        hideLoading()
                    }

                    is UiState.Loading -> {
                        Log.d(TAG, "onViewCreated: ")
                        showLoading()
                    }

                    UiState.Idle -> {
                        hideLoading()
                    }
                }
            }
        }

        val adapter = WatchListAdapter(onRemoveListner = { product ->
            val dialog = DailogFramgent(onDialogPositiveClick = {
                viewModel.removeProductFromWatchList(product.id.substring(22))
            }, onDialogNegativeClick = {
                findNavController().navigate(
                    R.id.action_settingsFragment_to_productInfoFragment)
            })
            dialog.show(requireActivity().supportFragmentManager, "dialog")
        }, onItemClickListener = {

        }, addToCart = { product: ProductNode ->
            viewModel.addToCart(product.variants.edges[0].node.title, 1)
        })

        binding.rvWishlist.adapter = adapter

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.watchlist.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            showLoading()
                        }

                        is UiState.Success -> {
                            hideLoading()
                            val watchlist = uiState.data
                            val limitedWatchlist =
                                if (watchlist.size > 2) watchlist.take(2) else watchlist
                            adapter.submitList(limitedWatchlist)
                        }

                        is UiState.Error -> {
                            hideLoading()
                            // Handle the error state, e.g., show an error message
                        }

                        UiState.Idle -> {
                            // Handle the idle state if necessary
                        }
                    }
                }
            }
        }

        observe()

        binding.ivMoreWishlist.setOnClickListener {
            findNavController().navigate(
                R.id.action_settingsFragment_to_wishListFragment)
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.ordersState.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        Log.i("observeOrders", "observeOrders: ${state.data}")
                        if (state.data.isEmpty()) {
                            return@collect
                        }

                        val orders = if (state.data.size > 2) state.data.subList(0, 2)
                        else state.data

                        val ordersAdapter = OrderAdapter(orders, this@SettingsFragment)
                        binding.rvOrders.adapter = ordersAdapter
                    }

                    is UiState.Error -> {

                    }

                    UiState.Loading -> {
                        // Show a loading indicator
                    }

                    UiState.Idle -> {
                        // Initial state, do nothing
                    }
                }
            }
        }
    }

    override fun onStart() {
        viewModel.fetchCurrencies()
        super.onStart()
    }

    private fun setListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
        binding.cvAddress.setOnClickListener {
            findNavController().navigate(
                R.id.action_settingsFragment_to_addAddressFragment)
        }

        binding.cvCurrency.setOnClickListener {
            findNavController().navigate(
                R.id.action_settingsFragment_to_currenciesFragment)
        }
        binding.cvOrder.setOnClickListener {
            findNavController().navigate(
                R.id.action_settingsFragment_to_orderFragment)
        }
        binding.cvWish.setOnClickListener {
            findNavController().navigate(
                R.id.action_settingsFragment_to_wishListFragment)
        }
    }

    private fun showLoading() {
        binding.lottieLoading.visibility = View.VISIBLE
        binding.mainContent.visibility = View.GONE
        binding.lottieLoading.playAnimation()
    }

    private fun hideLoading() {
        binding.lottieLoading.visibility = View.GONE
        binding.mainContent.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onOrderClick(order: MyOrder) {
        // Not Handled Yet
    }
}
