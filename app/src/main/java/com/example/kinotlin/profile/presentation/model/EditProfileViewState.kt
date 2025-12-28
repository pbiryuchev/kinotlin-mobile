package com.example.kinotlin.profile.presentation.model

data class EditProfileViewState(
    val fullName: String = "",
    val avatarUri: String? = null,
    val resumeUrl: String = "",
    val position: String = "",
)
