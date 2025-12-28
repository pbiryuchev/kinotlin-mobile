package com.example.kinotlin.profile.presentation.model

data class ProfileUiModel(
    val fullName: String = "",
    val avatarUri: String? = null,
    val resumeUrl: String = "",
    val position: String = "",
)
