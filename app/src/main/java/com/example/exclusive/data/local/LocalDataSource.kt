package com.example.exclusive.data.local



import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "LocalDataSource"
@Singleton
class LocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ILocalDataSource {
    companion object {
        val TOKEN_KEY = stringPreferencesKey("user_token")
        val Email_KEY = stringPreferencesKey("user_email")
        val USER_CART = stringPreferencesKey("user_cart")
        val USER_CHECKOUT = stringPreferencesKey("user_checkout")
        val USER_CURRENCY = stringPreferencesKey("user_currency")
        val USER_CURRENCY_VALUE = doublePreferencesKey("user_currency_value")
    }

    val token: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[TOKEN_KEY]
        }

    val userCart: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[USER_CART]
        }
    override suspend fun saveEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[Email_KEY] = email
        }
    }
    override suspend fun readEmail(): String? {
        return dataStore.data
            .map { preferences ->
                preferences[Email_KEY]
            }.first()
    }
   override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }
    override suspend fun readToken(): String? {
        return dataStore.data
            .map { preferences ->
                preferences[TOKEN_KEY]
            }.first()
    }

    override suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    override suspend fun saveUserCartId(cartId: String) {
        dataStore.edit { preferences ->
            preferences[USER_CART] = cartId
        }
    }

    override suspend fun saveCurrency(currency: String, currencyValue: Double) {
        dataStore.edit { preferences ->
            preferences[USER_CURRENCY] = currency
            preferences[USER_CURRENCY_VALUE] = currencyValue
        }
        Log.d(TAG, "saveCurrency: $currency $currencyValue")
    }
    override suspend fun getCurrency(): Pair<String, Double> {
        return dataStore.data
            .map { preferences ->
                val currency = preferences[USER_CURRENCY] ?: "EGY"
                val value = preferences[USER_CURRENCY_VALUE] ?: 1.0
                Pair(currency, value)
            }.first()
    }
    override suspend fun getUserCartId(): String? {
        return dataStore.data
            .map { preferences ->
                preferences[USER_CART]
               }.first()
       }
    override suspend fun getUserCheckOut(): String? {
        return dataStore.data
            .map { preferences ->
                preferences[USER_CHECKOUT]
            }.first()
    }
    override suspend fun saveUserCheckOut(checkoutId: String) {
        dataStore.edit { preferences ->
            preferences[USER_CHECKOUT] = checkoutId
        }
    }
    override suspend fun clearEmailAndToken() {
        dataStore.edit { preferences ->
            preferences.remove(Email_KEY)
            preferences.remove(TOKEN_KEY)
        }
    }
}
