package com.example.kinotlin.titles.presentation.model

data class TitleDetailsViewState (
    val title: TitleUiModel,
    val userRating: Int? = null,
    val isFavorite: Boolean = false,
) {
    val userVoteVisible get() = userRating != null
}