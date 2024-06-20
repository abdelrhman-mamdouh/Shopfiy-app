package com.example.fake

import com.example.exclusive.data.model.Currencies
import com.example.exclusive.data.remote.ICurrencyRemoteDataSource
import com.example.exclusive.data.remote.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeCurrencyRemoteDataSource : ICurrencyRemoteDataSource {
    private val currenciesFlow = MutableStateFlow<UiState<Currencies>>(UiState.Idle)

    override fun getCurrencies(currencyCode: String): Flow<UiState<Currencies>> {
        return currenciesFlow
    }

    fun setCurrenciesState(uiState: UiState<Currencies>) {
        currenciesFlow.value = uiState
    }
}