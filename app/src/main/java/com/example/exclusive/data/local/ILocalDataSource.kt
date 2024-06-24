package com.example.exclusive.data.local

interface ILocalDataSource {
    suspend fun saveToken(token: String)
    suspend fun clearToken()
    suspend fun readToken(): String?

    suspend fun saveUserCartId(cartId: String)
    suspend fun saveCurrency(currency: String, currencyValue: Double)
    suspend fun getCurrency(): Pair<String, Double>
    suspend fun saveEmail(email: String)
    suspend fun readEmail(): String?
    suspend fun getUserCartId(): String?
    suspend fun getUserCheckOut(): String?
    suspend fun saveUserCheckOut(checkoutId: String)
    suspend fun clearEmailAndToken()
    suspend fun updateIsGuest(isGuest: Boolean)
    suspend fun getIsGuest(): Boolean
}