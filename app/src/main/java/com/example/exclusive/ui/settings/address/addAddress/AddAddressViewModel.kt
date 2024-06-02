package com.example.exclusive.ui.settings.address.addAddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.model.AddressInput
import com.example.exclusive.type.MailingAddressInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor(
    private val remoteDataSource: ShopifyRemoteDataSource,
    private val localDataSource: LocalDataSource
) : ViewModel() {

    private val _addAddressResult = MutableStateFlow<UiState<Boolean>>(UiState.Idle)
    val addAddressResult: StateFlow<UiState<Boolean>> = _addAddressResult

    fun addAddress(address: MailingAddressInput) {
        viewModelScope.launch {
            _addAddressResult.value = UiState.Loading
            try {
                val customerAccessToken = localDataSource.readToken()!!
                val success = remoteDataSource.addAddress(address, customerAccessToken)
                _addAddressResult.value = UiState.Success(success)
            } catch (e: Exception) {
                _addAddressResult.value = UiState.Error(e)
            }
        }
    }
}