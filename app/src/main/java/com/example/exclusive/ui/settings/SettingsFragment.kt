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
import com.example.exclusive.AuthMain
import com.example.exclusive.HolderActivity
import com.example.exclusive.data.local.LocalDataSource
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
        binding.outlinedButton.setOnClickListener {
            viewModel.clearEmailAndToken()

            val intent = Intent(requireContext(), AuthMain::class.java)
            startActivity(intent)

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
            val dialog = DailogFramgent(
                onDialogPositiveClick = {
                    viewModel.removeProductFromWatchList(product.id.substring(22))
                },
                onDialogNegativeClick = {
                    val intent = Intent(requireContext(), HolderActivity::class.java).apply {
                        putExtra(HolderActivity.GO_TO, "INFO")
                    }
                    startActivity(intent)
                }
            )
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
                            adapter.submitList(uiState.data)
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
            val intent = Intent(requireContext(), HolderActivity::class.java).apply {
                putExtra(HolderActivity.GO_TO, "FAV")
            }
            startActivity(intent)
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

                        val orders =
                            if (state.data.size>2)
                            state.data.subList(0, 2)
                        else
                            state.data

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
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
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
        binding.tvOrders.setOnClickListener {
            val intent = Intent(requireContext(), HolderActivity::class.java).apply {
                putExtra(HolderActivity.GO_TO, "ORDERS")
            }
            startActivity(intent)
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