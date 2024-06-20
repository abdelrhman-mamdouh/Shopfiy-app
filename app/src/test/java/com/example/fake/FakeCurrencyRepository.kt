package com.example.fake

import com.example.exclusive.data.model.Currencies
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.data.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeCurrencyRepository : CurrencyRepository {
    private val currenciesFlow = MutableStateFlow<UiState<Currencies>>(UiState.Idle)
    private val currentCurrencyFlow = MutableStateFlow(Pair("USD", 1.0))

    override fun getCurrencies(currencyCode: String): Flow<UiState<Currencies>> {
        return currenciesFlow
    }

    override suspend fun saveCurrency(pair: Pair<String, Double>) {
        // Simulate saving currency locally
        currentCurrencyFlow.value = pair
    }

    override fun getCurrentCurrency(): Flow<Pair<String, Double>> {
        return currentCurrencyFlow
    }

    // Helper method to set currencies flow value for testing
    fun setCurrenciesState(uiState: UiState<Currencies>) {
        currenciesFlow.value = uiState
    }
}