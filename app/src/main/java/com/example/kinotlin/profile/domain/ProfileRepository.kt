package com.example.kinotlin.profile.domain

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeProfile(): Flow<Profile>
    suspend fun saveProfile(profile: Profile)
}
