package com.example.exclusive.ui.settings.address.addAddress

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.data.repository.AddressRepository
import com.example.exclusive.model.AddressInput
import com.example.exclusive.type.MailingAddressInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AddAddressViewModel"
@HiltViewModel
class AddAddressViewModel @Inject constructor(
    private val addressRepository: AddressRepository
) : ViewModel() {

    private val _addAddressResult = MutableStateFlow<UiState<Boolean>>(UiState.Idle)
    val addAddressResult: StateFlow<UiState<Boolean>> = _addAddressResult

    fun addAddress(address: MailingAddressInput) {
        viewModelScope.launch {
            _addAddressResult.value = UiState.Loading
            try {
                val success = addressRepository.addCustomerAddress(address)
                _addAddressResult.value = UiState.Success(success)
            } catch (e: Exception) {
                _addAddressResult.value = UiState.Error(e)
            }
        }
    }

    private val _deleteAddressResult = MutableStateFlow<UiState<Boolean>>(UiState.Idle)
    val deleteAddressResult: StateFlow<UiState<Boolean>> = _deleteAddressResult

    fun deleteAddress(addressId: String) {
        viewModelScope.launch {
            _deleteAddressResult.value = UiState.Loading
            try {
                val success = addressRepository.deleteCustomerAddress(addressId)
                _deleteAddressResult.value = UiState.Success(success)
            } catch (e: Exception) {
                _deleteAddressResult.value = UiState.Error(e)
            }
        }
    }


}
