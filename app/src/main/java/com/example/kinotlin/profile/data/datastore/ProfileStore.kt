package com.example.kinotlin.profile.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.kinotlin.profile.domain.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileStore(
    private val dataStore: DataStore<Preferences>,
) {
    private val fullNameKey = stringPreferencesKey(KEY_FULL_NAME)
    private val avatarUriKey = stringPreferencesKey(KEY_AVATAR_URI)
    private val resumeUrlKey = stringPreferencesKey(KEY_RESUME_URL)
    private val positionKey = stringPreferencesKey(KEY_POSITION)

    fun profileFlow(): Flow<Profile> = dataStore.data.map { prefs ->
        Profile(
            fullName = prefs[fullNameKey] ?: "",
            avatarUri = prefs[avatarUriKey],
            resumeUrl = prefs[resumeUrlKey] ?: "",
            position = prefs[positionKey] ?: "",
        )
    }

    suspend fun save(profile: Profile) {
        dataStore.edit { prefs ->
            prefs[fullNameKey] = profile.fullName
            prefs[resumeUrlKey] = profile.resumeUrl
            prefs[positionKey] = profile.position

            val avatarUri = profile.avatarUri
            if (avatarUri.isNullOrBlank()) {
                prefs.remove(avatarUriKey)
            } else {
                prefs[avatarUriKey] = avatarUri
            }
        }
    }

    private companion object {
        private const val KEY_FULL_NAME = "profile_full_name"
        private const val KEY_AVATAR_URI = "profile_avatar_uri"
        private const val KEY_RESUME_URL = "profile_resume_url"
        private const val KEY_POSITION = "profile_position"
    }
}
