package com.example.practicesandroid.drivers.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.practicesandroid.drivers.data.api.DriversApi
import com.example.practicesandroid.drivers.data.mapper.DriversResponseToEntityMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DriversRepository(
    private val api: DriversApi,
    private val mapper: DriversResponseToEntityMapper,
    private val dataStore: DataStore<Preferences>
) {
    suspend fun getDrivers() = withContext(Dispatchers.IO) {
        val response = api.getDrivers()
        mapper.mapResponse(response)
    }

    suspend fun getDriverById(driverId: String) = withContext(Dispatchers.IO) {
        val response = api.getDriverById(driverId)
        mapper.mapDriverDetailsResponse(response)
    }

    val sortOrderFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[SORT_ORDER_KEY] ?: false
        }

    suspend fun updateSortOrder(isAscending: Boolean) {
        dataStore.edit { preferences ->
            preferences[SORT_ORDER_KEY] = isAscending
        }
    }

    companion object {
        private val SORT_ORDER_KEY = booleanPreferencesKey("sort_order_ascending")
    }
}

