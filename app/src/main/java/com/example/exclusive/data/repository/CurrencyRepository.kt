package com.example.exclusive.data.repository

import com.example.exclusive.data.model.Currencies
import com.example.exclusive.data.remote.UiState
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun getCurrencies(currencyCode: String): Flow<UiState<Currencies>>

    suspend fun saveCurrency(pair: Pair<String, Double>)
    fun getCurrentCurrency(): Flow<Pair<String, Double>>
}