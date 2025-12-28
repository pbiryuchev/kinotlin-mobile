package com.example.kinotlin.profile.data.repository

import com.example.kinotlin.profile.data.datastore.ProfileStore
import com.example.kinotlin.profile.domain.Profile
import com.example.kinotlin.profile.domain.ProfileRepository
import kotlinx.coroutines.flow.Flow

class LocalProfileRepository(
    private val store: ProfileStore,
) : ProfileRepository {
    override fun observeProfile(): Flow<Profile> = store.profileFlow()

    override suspend fun saveProfile(profile: Profile) {
        store.save(profile)
    }
}
