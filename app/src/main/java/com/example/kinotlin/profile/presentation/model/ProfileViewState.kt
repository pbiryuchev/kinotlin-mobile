package com.example.kinotlin.profile.presentation.model

import com.example.kinotlin.profile.domain.Profile

data class ProfileViewState(
    val profile: Profile = Profile(),
)
