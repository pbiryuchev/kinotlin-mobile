package com.example.kinotlin.titles.data.favorites

import com.example.kinotlin.titles.presentation.model.TitleUiModel
import com.example.kinotlin.titles.presentation.model.TitleType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepository(
    private val dao: FavoritesDao,
) {

    fun observeFavorites(): Flow<List<TitleUiModel>> = dao.observeAll().map { list ->
        list.map { it.toUiModel() }
    }

    suspend fun isFavorite(id: String): Boolean = dao.isFavorite(id)

    suspend fun add(title: TitleUiModel) {
        dao.upsert(title.toEntity())
    }

    suspend fun remove(id: String) {
        dao.deleteById(id)
    }

    private fun FavoriteTitleEntity.toUiModel(): TitleUiModel {
        return TitleUiModel(
            id = id,
            type = runCatching { TitleType.valueOf(type) }.getOrDefault(TitleType.MOVIE),
            primaryTitle = primaryTitle,
            originalTitle = primaryTitle,
            startYear = startYear,
            runtimeSeconds = null,
            genres = emptyList(),
            plot = "",
            rating = TitleUiModel.Rating(
                aggregateRating = rating,
                voteCount = voteCount,
            ),
            primaryImage = TitleUiModel.Image(
                url = imageUrl,
                width = imageWidth,
                height = imageHeight,
            ),
        )
    }

    private fun TitleUiModel.toEntity(): FavoriteTitleEntity {
        return FavoriteTitleEntity(
            id = id,
            primaryTitle = primaryTitle,
            type = type.name,
            startYear = startYear,
            imageUrl = primaryImage.url,
            imageWidth = primaryImage.width,
            imageHeight = primaryImage.height,
            rating = rating.aggregateRating,
            voteCount = rating.voteCount,
        )
    }
}
