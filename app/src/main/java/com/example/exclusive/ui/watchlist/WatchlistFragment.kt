package com.example.exclusive.ui.watchlist

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exclusive.R
import com.example.exclusive.databinding.FragmentWatchlistBinding
import com.example.exclusive.model.ProductNode
import com.example.exclusive.ui.productinfo.DailogFramgent
import com.example.exclusive.utilities.Constants
import com.example.exclusive.utilities.SnackbarUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WatchlistFragment : Fragment() {
    lateinit var binding: FragmentWatchlistBinding
    private val viewModel: WatchlistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchWatchlist()
        binding.titleBar.tvTitle.text = getString(R.string.wish_list)
        binding.titleBar.icBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack();

        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().supportFragmentManager.popBackStack();
                }
            })
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isGuest.collect {
                    if (it) {
                        binding.tvGoToLogin.visibility = View.VISIBLE
                        binding.guestMode.visibility = View.VISIBLE
                        binding.zeroItems.visibility = View.GONE
                        binding.rvWatchlist.visibility = View.GONE

                    } else {
                        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
                            object : OnBackPressedCallback(true) {
                                override fun handleOnBackPressed() {
                                    parentFragmentManager.popBackStack()
                                }
                            })
                        val adapter = WatchListAdapter(removeItem, onItemClick, addItemToCart)
                        binding.rvWatchlist.adapter = adapter
                        binding.rvWatchlist.layoutManager = LinearLayoutManager(requireContext())
                        lifecycleScope.launch {
                            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                                viewModel.watchlist.collect {
                                    if (it.isEmpty()) {
                                        binding.tvGoToLogin.visibility = View.GONE
                                        binding.guestMode.visibility = View.GONE
                                        binding.zeroItems.visibility = View.VISIBLE
                                        binding.rvWatchlist.visibility = View.GONE
                                    } else {
                                        binding.tvGoToLogin.visibility = View.GONE
                                        binding.guestMode.visibility = View.GONE
                                        binding.zeroItems.visibility = View.GONE
                                        binding.rvWatchlist.visibility = View.VISIBLE
                                    }
                                    adapter.submitList(it)
                                }
                            }
                        }

                    }
                }
            }
        }

        binding.tvGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_watchlistFragment_to_loginFragment)
        }

    }

    private val removeItem: (ProductNode) -> Unit = { product ->
        showRemoveFromFavoritesDialog(R.drawable.gif,product)

    }

    private val addItemToCart = { product: ProductNode ->

        if(product.variants.edges[0].node.quantityAvailable>1){
            viewModel.addToCart(
                product.variants.edges[0].node.id, product.variants.edges[0].node.quantityAvailable
            )
            SnackbarUtils.showSnackbar(requireContext(),requireView(),"Product Added to Cart")
        }else{
            SnackbarUtils.showSnackbar(requireContext(),requireView(),"Out of Stock")
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
    private val onItemClick = { product: ProductNode ->
        NavHostFragment.findNavController(this@WatchlistFragment).navigate(
            WatchlistFragmentDirections.actionWatchlistFragmentToProductInfoFragment(product)
        )
    }
}