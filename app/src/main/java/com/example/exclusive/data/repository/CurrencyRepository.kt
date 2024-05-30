package com.example.exclusive.data.repository

import android.util.Log
import com.example.exclusive.data.model.Currencies
import com.example.exclusive.data.remote.CurrencyRemoteDataSource
import com.example.exclusive.data.remote.UiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CurrencyRepository @Inject constructor(private val currencyRemoteDataSource: CurrencyRemoteDataSource) {
    fun getCurrencies(): Flow<UiState<Currencies>> {
        Log.d("CurrencyRepository", "getCurrencies: Fetching currency rates")
        return currencyRemoteDataSource.getCurrencies()
    }
}