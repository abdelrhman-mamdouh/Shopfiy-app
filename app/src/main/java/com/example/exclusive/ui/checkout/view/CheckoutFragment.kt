// CheckoutFragment.kt
package com.example.exclusive.ui.checkout.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exclusive.R
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.DialogAddressSelectionBinding
import com.example.exclusive.databinding.FragmentCheckOutBinding
import com.example.exclusive.model.AddressInput
import com.example.exclusive.model.BillingAddress
import com.example.exclusive.model.DiscountValue
import com.example.exclusive.model.LineItem
import com.example.exclusive.ui.checkout.viewmodel.CheckoutViewModel
import com.example.exclusive.utilities.SnackbarUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "TAG"

@AndroidEntryPoint
class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckOutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CheckoutViewModel by viewModels()
    private val checkoutViewModel: CheckoutViewModel by viewModels()
    private lateinit var billingAddress: BillingAddress
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
            parentFragmentManager.popBackStack()
            findNavController().navigate(R.id.action_checkoutFragment_to_homeFragment)
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    parentFragmentManager.popBackStack()
                    findNavController().navigate(R.id.action_checkoutFragment_to_homeFragment)
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
                SnackbarUtils.showSnackbar(
                    requireContext(), requireView(), "Enter Discount Code"
                )
            }
        }
        observeCheckOutDetails()
        observeAddressesViewModel()
    }

    private fun observeCheckOutDetails() {
        checkoutViewModel.fetchCheckoutDetails()
        lifecycleScope.launch {
            checkoutViewModel.checkoutDetailsState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val checkoutDetails = state.data
                        val discountValue =
                            checkoutDetails.discountApplications.firstOrNull()?.value
                        Log.i(TAG, "observeCheckOutDetails: ${state.data.webUrl}")
                        if (discountValue is DiscountValue.Percentage) {
                            binding.tvDiscountPrice.text = "${discountValue.percentage}%"
                        } else {
                            binding.tvDiscountPrice.text = "N/A"
                        }
                        binding.tvOrderPrice.text =
                            "${calculateTotalPrice(checkoutDetails.lineItems)} ${checkoutDetails.totalPrice?.currencyCode}"
                        binding.tvSummaryPrice.text =
                            "${checkoutDetails.totalPrice?.amount} ${checkoutDetails.totalPrice?.currencyCode}"

                        setSubmitOrderClickListener(state.data.webUrl)
                    }

                    is UiState.Error -> {
                        val errorMessage = state.exception.message
                        // Handle error state
                    }

                    UiState.Idle -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setSubmitOrderClickListener(webUrl: String) {

        binding.btnSubmitOrder.setOnClickListener {
            val selectedPaymentMethod = getSelectedPaymentMethod()
            if (selectedPaymentMethod == "payment not selected") {
                SnackbarUtils.showSnackbar(
                    requireContext(),
                    requireView(),
                    "Select payment way"
                )
            } else {
                if (selectedPaymentMethod == "Credit Card") {
                    val action =
                        CheckoutFragmentDirections.actionCheckoutFragmentToCheckoutWebVieFragment(
                            webUrl
                        )
                    findNavController().navigate(action)
                } else if (selectedPaymentMethod == "Cash On Delivery") {
                    createOrder()
                }
            }
        }
    }

    private fun getSelectedPaymentMethod(): String {
        val radioButtonId = binding.paymentRadioGroup.checkedRadioButtonId
        if (radioButtonId != View.NO_ID) {
            val radioButton = binding.paymentRadioGroup.findViewById<RadioButton>(radioButtonId)
            return radioButton.text.toString()
        }
        return "payment not selected"
    }

    private fun createOrder() {
        if (::billingAddress.isInitialized) {
            lifecycleScope.launch {
                val success = checkoutViewModel.createOrder(billingAddress = billingAddress)
                if (success) {
                    parentFragmentManager.popBackStack()
                    findNavController().navigate(R.id.action_checkoutFragment_to_paymentCompletedFragment)
                } else {
                    SnackbarUtils.showSnackbar(
                        requireContext(),
                        requireView(),
                        "Failed to create order"
                    )
                }
            }
        } else {
            SnackbarUtils.showSnackbar(
                requireContext(),
                requireView(),
                "Please Select Address"
            )
        }
    }

    private fun showAddressSelectionDialog() {
        val dialogBinding = DialogAddressSelectionBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(dialogBinding.root)
        checkoutViewModel.addresses.value.let { state ->
            if (state is UiState.Success) {
                val addressAdapter = AddressAdapter(state.data) { address ->
                    val address = AddressInput(
                        firstName = address.firstName,
                        address1 = address.address1,
                        phone = address.phone,
                        city = address.city,
                        country = address.country,
                        zip = address.zip
                    )
                    billingAddress = BillingAddress(
                        address1 = address.address1,
                        city = address.city,
                        first_name = address.firstName,
                        phone = address.phone,
                        last_name = address.firstName,
                        zip = address.zip,
                        country = address.country, province = address.province
                    )
                    Log.i(
                        "showAddressSelectionDialog",
                        "showAddressSelectionDialog:${billingAddress} "
                    )
                    var mailingAddressInput = address.toInput()
                    lifecycleScope.launch {
                        val success = checkoutViewModel.applyShippingAddress(mailingAddressInput)
                        if (success) {
                            binding.tvShippingAddressDetails.text =
                                "${address.address1}, ${address.city}, ${address.country}"
                            binding.tvPhoneNumber.text = address.phone
                            SnackbarUtils.showSnackbar(
                                requireContext(), requireView(), "Address Added"
                            )

                            observeCheckOutDetails()
                            dialog.dismiss()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Error applying address",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                dialogBinding.rvAddresses.layoutManager = LinearLayoutManager(requireContext())
                dialogBinding.rvAddresses.adapter = addressAdapter
            }
        }
        dialog.show()
    }

    private fun observeAddressesViewModel() {
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

                        val addresses = state.data
                        if (addresses.isNotEmpty()) {
                            val defaultAddress = addresses.first()
                            binding.tvShippingAddressDetails.text =
                                "${defaultAddress.address1}, ${defaultAddress.city}, ${defaultAddress.country}"
                            binding.tvPhoneNumber.text = defaultAddress.phone
                            binding.btnChangeAddress.setOnClickListener {
                                showAddressSelectionDialog()
                            }

                            if (!::billingAddress.isInitialized) {
                                billingAddress = BillingAddress(
                                    address1 = defaultAddress.address1,
                                    city = defaultAddress.city,
                                    first_name = defaultAddress.firstName,
                                    phone = defaultAddress.phone,
                                    last_name = defaultAddress.firstName,
                                    zip = defaultAddress.zip,
                                    country = defaultAddress.country,
                                    province = defaultAddress.province
                                )
                                val mailingAddressInput = defaultAddress.toInput()
                                lifecycleScope.launch {
                                    val success =
                                        checkoutViewModel.applyShippingAddress(mailingAddressInput)
                                    if (success) {
                                        SnackbarUtils.showSnackbar(
                                            requireContext(),
                                            requireView(),
                                            "Default Address Applied"
                                        )
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Error applying default address",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }else{
                            binding.btnChangeAddress.setOnClickListener{
                                SnackbarUtils.showSnackbar(
                                    requireContext(),
                                    requireView(),
                                    "No addresses available. Please add an address."
                                )
                            }
                            SnackbarUtils.showSnackbar(
                                requireContext(),
                                requireView(),
                                "No addresses available. Please add an address."
                            )
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

    private fun calculateTotalPrice(lineItems: List<LineItem>): Double {
        var totalPrice = 0.0
        for (lineItem in lineItems) {
            val priceAmount = lineItem.variant.price.amount.toDouble()
            totalPrice += lineItem.quantity * priceAmount
        }
        return totalPrice
    }
}