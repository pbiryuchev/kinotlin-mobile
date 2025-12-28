package com.example.kinotlin.titles.presentation.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

// https://imdbapi.dev/

@Serializable
data class TitleUiModel(
    val id: String,
    val type: TitleType,
    val primaryTitle: String,
    val originalTitle: String,
    val primaryImage: Image,
    val startYear: Int,
    val endYear: Int? = null,
    val runtimeSeconds: Int? = null,
    val genres: List<String>,
    val rating: Rating,
    val plot: String
) {
    @Serializable
    data class Image(
        val url: String,
        val width: Int,
        val height: Int
    )

    @Serializable
    data class Rating(
        val aggregateRating: Double,
        val voteCount: Int
    )
}

@Serializable
enum class TitleType {
    @SerialName("movie")
    MOVIE,

    @SerialName("tvSeries")
    TV_SERIES,

    @SerialName("tvMiniSeries")
    TV_MINI_SERIES,

    @SerialName("tvMovie")
    TV_MOVIE,

    @SerialName("tvSpecial")
    TV_SPECIAL,

    @SerialName("short")
    SHORT,

    @SerialName("video")
    VIDEO,

    @SerialName("videoGame")
    VIDEO_GAME
}
