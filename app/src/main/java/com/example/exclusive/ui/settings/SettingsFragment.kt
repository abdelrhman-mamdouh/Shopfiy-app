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
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.exclusive.R
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentSettingsBinding
import com.example.exclusive.model.MyOrder
import com.example.exclusive.model.ProductNode
import com.example.exclusive.ui.orders.view.OnOrderClickListener
import com.example.exclusive.ui.orders.view.OrderAdapter
import com.example.exclusive.ui.watchlist.WatchListAdapter
import com.example.exclusive.utilities.Constants
import com.example.exclusive.utilities.SnackbarUtils
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
            binding.aboutUs.setOnClickListener {
                Constants.showConfirmationDialog(
                    requireContext(),
                    R.drawable.about_us,
                    title = "Exclusive m-commerce android mobile application",
                    message = "PREPARED BY \n Abdelrahman Mamdouhn \n AbdulHameed Mohamed \n Mahmoud Osama \n Tasneem Ibraheem",
                    positiveButtonText = "Ok",
                    onPositiveClick = {

                    })
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
            observeWishList(binding)
        }
    }

    private fun observeWishList(binding: FragmentSettingsBinding) {
        val adapter = WatchListAdapter(onRemoveListner = { product ->
            showRemoveFromFavoritesDialog(R.drawable.gif, product)
        }, onItemClickListener = {

        }, addToCart = { product: ProductNode ->

            if(product.variants.edges[0].node.quantityAvailable>1){
                viewModel.addToCart(product.variants.edges[0].node.id, 1)
                SnackbarUtils.showSnackbar(requireContext(),requireView(),"Product Added to Cart")
            }else{
                SnackbarUtils.showSnackbar(requireContext(),requireView(),"Out of Stock")
            }
        })

        binding.rvWishlist.adapter = adapter

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.watchlist.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            showLoading(binding)
                        }

                        is UiState.Success -> {
                            hideLoading(binding)
                            adapter.submitList(uiState.data)
                        }

                        is UiState.Error -> {
                            hideLoading(binding)

                        }

                        UiState.Idle -> {

                        }
                    }
                }
            }
        }
    }

    private fun observe(binding: FragmentSettingsBinding) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.ordersState.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            hideLoading(binding)
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
                            showLoading(binding)
                        }

                        UiState.Idle -> {
                            hideLoading(binding)
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
        val navOptions = NavOptions.Builder().setPopUpTo(R.id.settingsFragment, false).build()

        binding.cvAddress.setOnClickListener {
            findNavController().navigate(
                R.id.action_settingsFragment_to_addressesFragment,
                null,
                navOptions
            )
        }

        binding.cvCurrency.setOnClickListener {
            findNavController().navigate(
                R.id.action_settingsFragment_to_currenciesFragment,
                null,
                navOptions
            )
        }

        binding.cvOrder.setOnClickListener {
            findNavController().navigate(
                R.id.action_settingsFragment_to_orderFragment,
                null,
                navOptions
            )
        }

        binding.cvWish.setOnClickListener {
            findNavController().navigate(
                R.id.action_settingsFragment_to_wishListFragment,
                null,
                navOptions
            )
        }
    }

    private fun showRemoveFromFavoritesDialog(gif: Int, productNode: ProductNode) {
        Constants.showConfirmationDialog(
            requireContext(),
            gif = gif,
            title = "Remove from Favorites",
            message = "Are you sure you want to remove this product from favorites?",
            positiveButtonText = "Yes",
            onPositiveClick = {

                viewModel.removeProductFromWatchList(productNode.id.substring(22))
                SnackbarUtils.showSnackbar(
                    requireContext(), binding.root, "Product removed from favorites"
                )
            })
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
