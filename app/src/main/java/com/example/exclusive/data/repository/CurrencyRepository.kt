package com.example.exclusive.data.repository

import android.util.Log
import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.model.Currencies
import com.example.exclusive.data.remote.CurrencyRemoteDataSource
import com.example.exclusive.data.remote.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val currencyRemoteDataSource: CurrencyRemoteDataSource,
    private val localDataSource: LocalDataSource) {
    fun getCurrencies(currencyCode: String): Flow<UiState<Currencies>> {
        Log.d("CurrencyRepository", "getCurrencies: Fetching currency rates")
        return currencyRemoteDataSource.getCurrencies(currencyCode)
    }

    suspend fun saveCurrency(pair: Pair<String, Double>) {
        localDataSource.saveCurrency(pair.first, pair.second)
    }

    fun getCurrentCurrency(): Flow<Pair<String, Double>> = flow {
        emit(localDataSource.getCurrency())
    }
}