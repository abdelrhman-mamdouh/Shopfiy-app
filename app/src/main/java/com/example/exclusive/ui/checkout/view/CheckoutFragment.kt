// CheckoutFragment.kt
package com.example.exclusive.ui.checkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exclusive.R
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.DialogAddressSelectionBinding
import com.example.exclusive.databinding.FragmentCheckOutBinding
import com.example.exclusive.model.DiscountValue
import com.example.exclusive.model.LineItem
import com.example.exclusive.ui.checkout.viewmodel.CheckoutViewModel
import com.example.exclusive.utilities.SnackbarUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "CheckoutFragment"

@AndroidEntryPoint
class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckOutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CheckoutViewModel by viewModels()
    private val checkoutViewModel: CheckoutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckOutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleBar.tvTitle.text = getString(R.string.check_out)
        binding.titleBar.icBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
        binding.btnApplyCode.setOnClickListener {
            val discountCode = binding.etDiscountCode.text.toString()
            if (discountCode.isNotBlank()) {
                lifecycleScope.launch {
                    val success = checkoutViewModel.applyDiscountCode(discountCode)
                    if (success) {
                        SnackbarUtils.showSnackbar(
                            requireContext(), requireView(), "Discount Added"
                        )
                        observeCheckOutDetails()
                    }
                }
            } else {

            }
        }
        binding.btnSubmitOrder.setOnClickListener {
        }
        checkoutViewModel.fetchCheckoutDetails()
        observeCheckOutDetails()
        observeViewModel()
    }

    private fun observeCheckOutDetails() {
        lifecycleScope.launch {
            checkoutViewModel.checkoutDetailsState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val checkoutDetails = state.data
                        val discountValue = checkoutDetails.discountApplications.firstOrNull()?.value

                        if (discountValue is DiscountValue.Percentage) {
                            binding.tvDiscountPrice.text = "${discountValue.percentage}%"
                        } else {
                            binding.tvDiscountPrice.text = "N/A"
                        }
                        binding.tvOrderPrice.text = calculateTotalPrice(checkoutDetails.lineItems).toString() + " " + checkoutDetails.totalPrice?.currencyCode
                        binding.tvSummaryPrice.text = checkoutDetails.totalPrice?.amount + " " + checkoutDetails.totalPrice?.currencyCode
                    }



                    is UiState.Error -> {
                        val errorMessage = state.exception.message
                    }

                    UiState.Idle -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun showAddressSelectionDialog() {
        val dialogBinding = DialogAddressSelectionBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(dialogBinding.root)

        checkoutViewModel.addresses.value.let { state ->
            if (state is UiState.Success) {
                val addressAdapter = AddressAdapter(state.data) { address ->
                    binding.tvShippingAddressDetails.text =
                        "${address.address1}, ${address.city}, ${address.country}"
                    binding.tvPhoneNumber.text = address.phone
                    dialog.dismiss()
                }
                dialogBinding.rvAddresses.layoutManager = LinearLayoutManager(requireContext())
                dialogBinding.rvAddresses.adapter = addressAdapter
            }
        }

        dialog.show()
    }

    private fun observeViewModel() {
        checkoutViewModel.fetchCustomerAddresses()
        viewLifecycleOwner.lifecycleScope.launch {
            checkoutViewModel.addresses.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.btnSubmitOrder.isEnabled = false
                    }

                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnSubmitOrder.isEnabled = true
                        binding.tvShippingAddressDetails.text =
                            "${state.data[0].address1}, ${state.data[0].city}, ${state.data[0].country}"
                        binding.tvPhoneNumber.text = state.data[0].phone
                        binding.btnChangeAddress.setOnClickListener {
                            showAddressSelectionDialog()
                        }
                    }

                    is UiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnSubmitOrder.isEnabled = true
                        Toast.makeText(
                            requireContext(), state.exception.message, Toast.LENGTH_SHORT
                        ).show()
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
    fun calculateTotalPrice(lineItems: List<LineItem>): Double {
        var totalPrice = 0.0
        for (lineItem in lineItems) {
            val priceAmount = lineItem.variant.price.amount.toDouble()
            totalPrice += lineItem.quantity * priceAmount
        }
        return totalPrice
    }
}
