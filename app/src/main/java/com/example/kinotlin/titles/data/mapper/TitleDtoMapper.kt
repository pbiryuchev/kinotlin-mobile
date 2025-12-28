package com.example.kinotlin.titles.data.mapper

import com.example.kinotlin.characters.presentation.model.TitleType
import com.example.kinotlin.characters.presentation.model.TitleUiModel
import com.example.kinotlin.titles.data.dto.TitleDto

class TitleDtoMapper {

    fun map(dto: TitleDto): TitleUiModel {
        val id = dto.id.orEmpty()

        return TitleUiModel(
            id = id,
            type = mapType(dto.type),
            primaryTitle = dto.primaryTitle ?: dto.originalTitle ?: "Без названия",
            originalTitle = dto.originalTitle ?: dto.primaryTitle ?: "Без названия",
            primaryImage = TitleUiModel.Image(
                url = dto.primaryImage?.url.orEmpty(),
                width = dto.primaryImage?.width ?: 0,
                height = dto.primaryImage?.height ?: 0,
            ),
            startYear = dto.startYear ?: 0,
            endYear = dto.endYear,
            runtimeSeconds = dto.runtimeSeconds,
            genres = dto.genres ?: emptyList(),
            rating = TitleUiModel.Rating(
                aggregateRating = dto.rating?.aggregateRating ?: 0.0,
                voteCount = dto.rating?.voteCount ?: 0,
            ),
            plot = dto.plot.orEmpty(),
        )
    }

    private fun mapType(raw: String?): TitleType {
        return when (raw) {
            "movie" -> TitleType.MOVIE
            "tvSeries" -> TitleType.TV_SERIES
            "tvMiniSeries" -> TitleType.TV_MINI_SERIES
            "tvMovie" -> TitleType.TV_MOVIE
            "tvSpecial" -> TitleType.TV_SPECIAL
            "short" -> TitleType.SHORT
            "video" -> TitleType.VIDEO
            "videoGame" -> TitleType.VIDEO_GAME
            else -> TitleType.MOVIE
        }
    }
}
