package com.example.exclusive.ui.cart

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.R
import com.example.exclusive.databinding.FragmentCartBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val cartViewModel: CartViewModel by viewModels()

    private lateinit var cartProductAdapter: CartProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleBar.tvTitle.text = getString(R.string.cart)
        binding.titleBar.icBack.setOnClickListener {
            requireActivity().finish()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            cartViewModel.cartProductsResponse.collect { response ->
                if (response != null) {
                    cartProductAdapter.submitList(response)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            cartViewModel.error.collect { error ->
                if (error != null) {
                    // Handle error
                }
            }
        }

        binding.buttonCheckout.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment)
        }
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        cartProductAdapter = CartProductAdapter()
        binding.rvCart.apply {
            adapter = cartProductAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        setupSwipeToDelete()
    }

    private fun setupSwipeToDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val removedItem = cartProductAdapter.removeItem(position)

                removedItem?.let {
                    viewLifecycleOwner.lifecycleScope.launch {
                        cartViewModel.deleteProductFromCart(it)
                    }
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvCart)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}