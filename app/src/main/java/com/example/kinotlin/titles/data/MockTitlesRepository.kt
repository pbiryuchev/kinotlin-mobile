package com.example.kinotlin.titles.data

import com.example.kinotlin.characters.presentation.MockData
import com.example.kinotlin.characters.presentation.model.TitleUiModel
import com.example.kinotlin.titles.domain.TitlesRepository

class MockTitlesRepository : TitlesRepository {
    private val titles: List<TitleUiModel> = MockData.getTitles()

    private val userRatings: MutableMap<String, Int> = mutableMapOf()

    override fun getTitles(): List<TitleUiModel> = titles

    override fun getTitleById(id: String): TitleUiModel? = titles.firstOrNull { it.id == id }

    override fun getUserRating(titleId: String): Int? = userRatings[titleId]

    override fun setUserRating(titleId: String, rating: Int?) {
        if (rating == null) {
            userRatings.remove(titleId)
        } else {
            userRatings[titleId] = rating
        }
    }
}
