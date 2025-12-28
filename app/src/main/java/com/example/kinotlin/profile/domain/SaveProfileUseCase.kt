package com.example.kinotlin.profile.domain

class SaveProfileUseCase(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(profile: Profile) {
        repository.saveProfile(profile)
    }
}
