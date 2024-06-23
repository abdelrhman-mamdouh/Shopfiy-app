package com.example.exclusive.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.exclusive.ui.orders.view.OnOrderClickListener
import com.example.exclusive.ui.orders.view.OrderAdapter
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
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.let { binding ->
            val swipeRefreshLayout = binding.swipeRefreshLayout
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                findNavController().popBackStack(R.id.settingsFragment, false)
                findNavController().navigate(R.id.settingsFragment)
            }

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
            setListeners(binding)
            binding.outlinedButton.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.clearEmailAndToken()
                    parentFragmentManager.popBackStack()
                    findNavController().navigate(
                        R.id.action_settingsFragment_to_loginFragment
                    )
                }
            }
            observe(binding)
        }
    }

    private fun observe(binding: FragmentSettingsBinding) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
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

                        is UiState.Error -> {}

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
    }

    override fun onStart() {
        viewModel.fetchCurrencies()
        super.onStart()
    }

    private fun setListeners(binding: FragmentSettingsBinding) {
        binding.cvAddress.setOnClickListener {
            findNavController().navigate(
                R.id.action_settingsFragment_to_addressesFragment
            )
        }

        binding.cvCurrency.setOnClickListener {
            findNavController().navigate(
                R.id.action_settingsFragment_to_currenciesFragment
            )
        }
        binding.cvOrder.setOnClickListener {
            findNavController().navigate(
                R.id.action_settingsFragment_to_orderFragment
            )
        }
        binding.cvWish.setOnClickListener {
            findNavController().navigate(
                R.id.action_settingsFragment_to_wishListFragment
            )
        }
    }

    private fun showLoading(binding: FragmentSettingsBinding) {
        binding.lottieLoading.visibility = View.VISIBLE
        binding.mainContent.visibility = View.GONE
        binding.lottieLoading.playAnimation()
    }

    private fun hideLoading(binding: FragmentSettingsBinding) {
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
