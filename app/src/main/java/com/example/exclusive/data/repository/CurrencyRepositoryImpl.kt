package com.example.exclusive.data.repository

import com.example.exclusive.data.local.ILocalDataSource
import com.example.exclusive.data.model.Currencies
import com.example.exclusive.data.remote.ICurrencyRemoteDataSource
import com.example.exclusive.data.remote.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val currencyRemoteDataSource: ICurrencyRemoteDataSource,
    private val localDataSource: ILocalDataSource
) : CurrencyRepository {
    override fun getCurrencies(currencyCode: String): Flow<UiState<Currencies>> {
        return currencyRemoteDataSource.getCurrencies(currencyCode)
    }

    override suspend fun saveCurrency(pair: Pair<String, Double>) {
        localDataSource.saveCurrency(pair.first, pair.second)
    }

    override fun getCurrentCurrency(): Flow<Pair<String, Double>> = flow {
        emit(localDataSource.getCurrency())
    }
}