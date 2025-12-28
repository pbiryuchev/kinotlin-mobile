package com.example.kinotlin.titles.domain

import com.example.kinotlin.characters.presentation.model.TitleUiModel

interface TitlesRepository {
    suspend fun getTitles(filter: TitlesFilter = TitlesFilter()): List<TitleUiModel>
    suspend fun getTitleById(id: String): TitleUiModel

    fun getUserRating(titleId: String): Int?
    fun setUserRating(titleId: String, rating: Int?)
}

data class TitlesFilter(
    val types: List<String>? = listOf("MOVIE"),
    val sortBy: String? = "SORT_BY_POPULARITY",
    val sortOrder: String? = "DESC",
    val minAggregateRating: Float? = null,
)
