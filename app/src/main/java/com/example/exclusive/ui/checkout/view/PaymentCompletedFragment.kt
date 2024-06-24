package com.example.exclusive.ui.checkout.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.exclusive.R
import com.example.exclusive.databinding.FragmentPaymentCompletedBinding
import com.example.exclusive.ui.cart.CartViewModel
import com.example.exclusive.utilities.SnackbarUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PaymentCompletedFragment : Fragment() {

    private var _binding: FragmentPaymentCompletedBinding? = null
    private val binding get() = _binding!!
    private val cartViewModel: CartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentCompletedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleBar.tvTitle.text = getString(R.string.payment_success)
        binding.titleBar.icBack.visibility = View.GONE

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                }
            })

        lifecycleScope.launch {
            cartViewModel.deleteAllProductsFromCart { isSuccess ->
                val message = if (isSuccess) {
                    "All products deleted successfully!"
                } else {
                    "Failed to delete products from cart."
                }
            }
        }

        binding.btnContinueShopping.setOnClickListener {
            val currentDestination = findNavController().currentDestination
            if (currentDestination?.id == R.id.paymentCompletedFragment) {
                findNavController().navigate(R.id.action_paymentCompletedFragment_to_orderFragment)
            } else {
                Log.e("TAG", "Current destination is not paymentCompletedFragment")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
