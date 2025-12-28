package com.example.kinotlin.titles.domain

import com.example.kinotlin.characters.presentation.model.TitleUiModel

interface TitlesRepository {
    fun getTitles(): List<TitleUiModel>
    fun getTitleById(id: String): TitleUiModel?

    fun getUserRating(titleId: String): Int?
    fun setUserRating(titleId: String, rating: Int?)
}
