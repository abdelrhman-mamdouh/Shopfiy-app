package com.example.exclusive.ui.checkout.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.data.model.AddressInput
import com.example.exclusive.data.model.CheckoutDetails

import com.example.exclusive.type.MailingAddressInput
import com.example.exclusive.ui.checkout.repository.CheckoutRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val checkoutRepository: CheckoutRepositoryImpl
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

}