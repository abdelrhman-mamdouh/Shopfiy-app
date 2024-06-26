package com.example.exclusive.ui.checkout.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.exclusive.R
import com.example.exclusive.data.model.LineItem
import com.example.exclusive.data.model.Order
import com.example.exclusive.data.model.OrderRequest
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.model.AddressInput
import com.example.exclusive.model.BillingAddress
import com.example.exclusive.model.CheckoutDetails
import com.example.exclusive.type.MailingAddressInput
import com.example.exclusive.ui.checkout.repository.CheckoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val checkoutRepository: CheckoutRepository
) : ViewModel() {

    private val _checkoutDetailsState = MutableStateFlow<UiState<CheckoutDetails>>(UiState.Idle)
    val checkoutDetailsState: StateFlow<UiState<CheckoutDetails>> = _checkoutDetailsState

    private val _addresses = MutableStateFlow<UiState<List<AddressInput>>>(UiState.Idle)
    val addresses: StateFlow<UiState<List<AddressInput>>> = _addresses
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    fun fetchCustomerAddresses() {
        viewModelScope.launch {
            _addresses.value = UiState.Loading
            try {
                val addresses = checkoutRepository.fetchCustomerAddresses()
                _addresses.value = UiState.Success(addresses)
            } catch (e: Exception) {
                _addresses.value = UiState.Error(e)
            }
        }
    }

    fun fetchCheckoutDetails() {
        viewModelScope.launch {
            _checkoutDetailsState.value = UiState.Loading
            try {
                val checkoutId = checkoutRepository.getUserCheckOut()
                val checkoutDetails = checkoutRepository.getCheckoutDetails(checkoutId!!)
                if (checkoutDetails != null) {
                    _checkoutDetailsState.value = UiState.Success(checkoutDetails)
                } else {
                    _checkoutDetailsState.value =
                        UiState.Error(Exception("Checkout details not found"))
                }
            } catch (e: Exception) {
                _checkoutDetailsState.value = UiState.Error(e)
            }
        }
    }

    suspend fun applyDiscountCode(discountCode: String): Boolean {
        val checkoutId = checkoutRepository.getUserCheckOut()
        return try {
            val success = checkoutRepository.applyDiscountCode(checkoutId!!, discountCode)
            if (success) {
                fetchCheckoutDetails()
            }
            success
        } catch (e: Exception) {
            Log.e("ApplyDiscount", "Error applying discount code: ${e.message}", e)
            false
        }
    }

    suspend fun applyShippingAddress(shippingAddress: MailingAddressInput): Boolean {

        val checkoutId = checkoutRepository.getUserCheckOut()
        return try {
            val success = checkoutRepository.applyShippingAddress(checkoutId!!, shippingAddress)
            if (success) {

            }
            success
        } catch (e: Exception) {
            Log.e("ApplyShippingAddress", "Error applying shipping address: ${e.message}", e)
            false
        }
    }

    suspend fun createOrder(billingAddress: BillingAddress): Boolean {
        try {
            val checkoutId = checkoutRepository.getUserCheckOut()
            val checkoutDetails = checkoutRepository.getCheckoutDetails(checkoutId!!)

            val regex = Regex("""\d+""")
            val orderRequest = OrderRequest(
                order = Order(
                    email = checkoutRepository.getUserEmail()!!,
                    send_receipt = true,
                    line_items = checkoutDetails?.lineItems?.map { lineItem ->
                        LineItem(
                            variant_id = regex.find(lineItem.variant.id)?.value ?: "", quantity = lineItem.quantity
                        )
                    }!!,
                    billing_address = billingAddress
                )
            )

            val orderResponse = checkoutRepository.createOrder(orderRequest)

            return true

        } catch (e: Exception) {

            _error.value = e.message

            return false
        }
    }


}