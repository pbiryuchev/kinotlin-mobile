package com.example.kinotlin.titles.data.repository

import com.example.kinotlin.characters.presentation.model.TitleUiModel
import com.example.kinotlin.titles.data.api.ImdbTitlesApi
import com.example.kinotlin.titles.data.mapper.TitleDtoMapper
import com.example.kinotlin.titles.domain.TitlesRepository
import com.example.kinotlin.titles.domain.TitlesFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkTitlesRepository(
    private val api: ImdbTitlesApi,
    private val mapper: TitleDtoMapper,
) : TitlesRepository {

    private val userRatings: MutableMap<String, Int> = mutableMapOf()

    override suspend fun getTitles(filter: TitlesFilter): List<TitleUiModel> = withContext(Dispatchers.IO) {
        api.listTitles(
            types = filter.types,
            sortBy = filter.sortBy,
            sortOrder = filter.sortOrder,
            minAggregateRating = filter.minAggregateRating,
        ).titles.map { mapper.map(it) }
    }

    override suspend fun getTitleById(id: String): TitleUiModel = withContext(Dispatchers.IO) {
        mapper.map(api.getTitle(id))
    }

    override fun getUserRating(titleId: String): Int? = userRatings[titleId]

    override fun setUserRating(titleId: String, rating: Int?) {
        if (rating == null) {
            userRatings.remove(titleId)
        } else {
            userRatings[titleId] = rating
        }
    }

    
}
