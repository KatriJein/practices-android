package com.example.profile.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.profile.domain.model.ProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import androidx.datastore.preferences.core.Preferences

class ProfileRepository(
    private val dataStore: DataStore<Preferences>
) {
    fun observeProfile(): Flow<ProfileEntity> {
        return dataStore.data.map { preferences ->
            ProfileEntity(
                fullName = preferences[FULL_NAME_KEY] ?: "",
                photoUri = preferences[PHOTO_URI_KEY] ?: "",
                resumeUrl = preferences[RESUME_URL_KEY] ?: "",
                time = preferences[TIME_KEY] ?: ""
            )
        }
    }

    suspend fun setProfile(
        fullName: String,
        photoUri: String,
        resumeUrl: String,
        time: String
    ) = withContext(Dispatchers.IO) {
        dataStore.edit { preferences ->
            preferences[FULL_NAME_KEY] = fullName
            preferences[PHOTO_URI_KEY] = photoUri
            preferences[RESUME_URL_KEY] = resumeUrl
            preferences[TIME_KEY] = time
        }
    }

    companion object {
        private val FULL_NAME_KEY = stringPreferencesKey("full_name_profile")
        private val PHOTO_URI_KEY = stringPreferencesKey("photo_uri_profile")
        private val RESUME_URL_KEY = stringPreferencesKey("resume_url_profile")
        private val TIME_KEY = stringPreferencesKey("time_profile")
    }
}