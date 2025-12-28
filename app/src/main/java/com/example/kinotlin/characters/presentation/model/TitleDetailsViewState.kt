package com.example.kinotlin.characters.presentation.model

data class TitleDetailsViewState (
    val title: TitleUiModel,
    val userRating: Int? = null,
) {
    val userVoteVisible get() = userRating != null
}