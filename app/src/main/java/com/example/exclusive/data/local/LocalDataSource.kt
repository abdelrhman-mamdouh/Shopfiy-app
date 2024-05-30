package com.example.exclusive.data.local



import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ILocalDataSource {
    companion object {
        val TOKEN_KEY = stringPreferencesKey("user_token")
    }

    val token: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[TOKEN_KEY]
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
}
