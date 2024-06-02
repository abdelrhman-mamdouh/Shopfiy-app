package com.example.exclusive.data.local

interface ILocalDataSource {
    suspend fun saveToken(token: String)
    suspend fun clearToken()
    suspend fun readToken(): String?
    suspend fun getUserCartId(): String?
    suspend fun saveUserCartId(cartId: String)
}