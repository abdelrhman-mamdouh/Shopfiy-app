package com.example.exclusive.data.remote

import com.example.exclusive.data.model.Currencies
import kotlinx.coroutines.flow.Flow

interface ICurrencyRemoteDataSource {
    fun getCurrencies(currencyCode: String): Flow<UiState<Currencies>>
}