package com.example.exclusive.ui.checkout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentCheckOutBinding
import com.example.exclusive.type.CheckoutLineItemInput
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "CheckoutFragment"
@AndroidEntryPoint
class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckOutBinding? = null
    private val binding get() = _binding!!

    private val checkoutViewModel: CheckoutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckOutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmitOrder.setOnClickListener {
            val lineItems = listOf(
                CheckoutLineItemInput(
                    quantity = 1,
                    variantId = "gid://shopify/ProductVariant/45323489870078"
                )
            )
            val email = "abdelrhman99m@gmail.com"
            checkoutViewModel.createCheckout(lineItems, email)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            checkoutViewModel.checkoutState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.btnSubmitOrder.isEnabled = false
                    }
                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnSubmitOrder.isEnabled = true
                        // Handle success, navigate to web URL or show success message
                        val webUrl = state.data.webUrl
                        Log.d(TAG, "observeViewModel: ${state.data.id}")
                        Log.d(TAG, "observeViewModel: ${state.data.lineItems}")
                        Toast.makeText(requireContext(), "Checkout Created: $webUrl", Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnSubmitOrder.isEnabled = true
                        // Show error message
                        Toast.makeText(requireContext(), state.exception.message, Toast.LENGTH_SHORT).show()
                    }
                    UiState.Idle -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnSubmitOrder.isEnabled = true
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}