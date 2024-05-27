package com.example.exclusive.data.remote

import com.example.exclusive.data.model.Currencies
import com.example.exclusive.data.network.CurrencyApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CurrencyRemoteDataSource @Inject constructor(private val currencyApiInterface: CurrencyApi) {

    fun getCurrencies(): Flow<UiState<Currencies>> = flow {
        emit(UiState.Loading)
        try {
            val response = currencyApiInterface.getCurrencies()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(UiState.Success(it))
                } ?: throw IOException("Response body is null")
            } else {
                throw HttpException(response)
            }
        } catch (e: HttpException) {
            emit(UiState.Error(e))
        } catch (e: IOException) {
            emit(UiState.Error(e))
        }
    }
}