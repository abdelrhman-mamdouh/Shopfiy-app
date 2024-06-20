package com.example.exclusive.data.local

import com.example.exclusive.data.local.ILocalDataSource

class FakeLocalDataSource : ILocalDataSource {

    private var currencyPair = Pair("USD", 1.0)

    override suspend fun saveCurrency(currency: String, currencyValue: Double) {
        currencyPair = Pair(currency, currencyValue)
    }

    override suspend fun getCurrency(): Pair<String, Double> {
        return currencyPair
    }

    override suspend fun saveToken(token: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearToken() {
        TODO("Not yet implemented")
    }

    override suspend fun readToken(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun saveUserCartId(cartId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun saveEmail(email: String) {
        TODO("Not yet implemented")
    }

    override suspend fun readEmail(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun getUserCartId(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun getUserCheckOut(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun saveUserCheckOut(checkoutId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearEmailAndToken() {
        TODO("Not yet implemented")
    }

    override suspend fun updateIsGuest(isGuest: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun getIsGuest(): Boolean {
        TODO("Not yet implemented")
    }
}