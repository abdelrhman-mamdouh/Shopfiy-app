package com.example.exclusive.ui.watchlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class WatchlistFragment : Fragment() {
    lateinit var binding: FragmentWatchlistBinding
    private val viewModel: WatchlistViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchWatchlist()
        val adapter = WatchListAdapter(removeItem,onItemClick,addItemToCart)
        binding.rvWatchlist.adapter = adapter
        binding.rvWatchlist.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.watchlist.collect{
                    adapter.submitList(it)
                }
            }
        }

    }
    val removeItem = {product: ProductNode ->
        viewModel.removeProductFromWatchList(product.id.substring(22))

    }
    val addItemToCart = {product: ProductNode ->

    }
    val onItemClick = {product: ProductNode ->
        NavHostFragment.findNavController(this@WatchlistFragment).navigate(WatchlistFragmentDirections.actionWatchlistFragmentToProductInfoFragment(product))
    }
}