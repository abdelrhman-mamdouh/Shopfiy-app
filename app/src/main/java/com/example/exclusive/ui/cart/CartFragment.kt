package com.example.exclusive.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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

        cartProductAdapter = CartProductAdapter()

        binding.rvCart.apply {
            adapter = cartProductAdapter
            layoutManager = LinearLayoutManager(requireContext())
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

        // Assuming you have a token to create the cart
        // cartViewModel.createCart("your_token_here")

        binding.buttonCheckout.setOnClickListener {
            // Handle checkout button click
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}