package com.example.kinotlin.titles.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TitleDto(
    val id: String? = null,
    val type: String? = null,
    val primaryTitle: String? = null,
    val originalTitle: String? = null,
    val primaryImage: ImageDto? = null,
    val startYear: Int? = null,
    val endYear: Int? = null,
    val runtimeSeconds: Int? = null,
    val genres: List<String>? = null,
    val rating: RatingDto? = null,
    val plot: String? = null,
)

@Serializable
data class ImageDto(
    val url: String? = null,
    val width: Int? = null,
    val height: Int? = null,
)

@Serializable
data class RatingDto(
    val aggregateRating: Double? = null,
    val voteCount: Int? = null,
)
