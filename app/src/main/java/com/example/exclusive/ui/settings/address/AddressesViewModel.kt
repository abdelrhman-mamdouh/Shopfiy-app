package com.example.exclusive.ui.settings.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.data.repository.AddressRepository
import com.example.exclusive.model.AddressInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressesViewModel @Inject constructor(
    private val addressRepository: AddressRepository
) : ViewModel() {

    private val _addresses = MutableStateFlow<UiState<List<AddressInput>>>(UiState.Idle)
    val addresses: StateFlow<UiState<List<AddressInput>>> = _addresses

    fun fetchCustomerAddresses() {
        viewModelScope.launch {
            _addresses.value = UiState.Loading
            try {
                val addresses = addressRepository.fetchCustomerAddresses()
                _addresses.value = UiState.Success(addresses)
            } catch (e: Exception) {
                _addresses.value = UiState.Error(e)
            }
        }
    }

    fun deleteAddress(addressId: String) {
        viewModelScope.launch {
            try {
                addressRepository.deleteCustomerAddress(addressId)
                fetchCustomerAddresses()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}