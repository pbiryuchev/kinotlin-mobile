package com.example.kinotlin.profile.domain

import kotlinx.coroutines.flow.Flow

class GetProfileUseCase(
    private val repository: ProfileRepository,
) {
    operator fun invoke(): Flow<Profile> = repository.observeProfile()
}
