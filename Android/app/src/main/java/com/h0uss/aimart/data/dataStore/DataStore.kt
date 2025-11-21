package com.h0uss.aimart.data.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_prefs"
)

class SessionManager {
    private lateinit var appContext: Context

    private lateinit var dataStore: DataStore<Preferences>

    fun init(context: Context): SessionManager {
        if (!::appContext.isInitialized) {
            this.appContext = context.applicationContext
            this.dataStore = appContext.userPreferencesDataStore
        }
        return this
    }

    companion object {
        val USER_ID_KEY = stringPreferencesKey("user_id")
        val USER_IS_SELLER_KEY = stringPreferencesKey("is_seller")
    }

    suspend fun saveUserId(userId: Long) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId.toString()
        }
    }

    suspend fun deleteUserId() {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = (-1L).toString()
        }
    }

    suspend fun saveUserIsSeller(isSeller: Boolean) {
        dataStore.edit { preferences ->
            preferences[USER_IS_SELLER_KEY] = isSeller.toString()
        }
    }

    suspend fun deleteUserIsSeller() {
        dataStore.edit { preferences ->
            preferences[USER_IS_SELLER_KEY] = false.toString()
        }
    }

    val userId: Flow<String?>
        get() = dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }
            .map { preferences ->
                preferences[USER_ID_KEY]
            }

    val isSeller: Flow<String?>
        get() = dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }
            .map { preferences ->
                preferences[USER_IS_SELLER_KEY]
            }
}