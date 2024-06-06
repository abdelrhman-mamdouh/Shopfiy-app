package com.example.exclusive.data.local

import com.example.exclusive.data.repository.CurrencyRepository

interface ILocalDataSource {
    suspend fun saveToken(token: String)
    suspend fun clearToken()
    suspend fun readToken(): String?
    suspend fun getUserCartId(): String?
    suspend fun saveUserCartId(cartId: String)
    suspend fun saveCurrency(currency: String, currencyValue: Double)
    suspend fun getCurrency(): Pair<String, Double>
    suspend fun saveEmail(email: String)
    suspend fun readEmail(): String?
}