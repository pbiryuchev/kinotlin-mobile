package com.example.kinotlin.titles.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListTitlesResponseDto(
    val titles: List<TitleDto> = emptyList(),
    val totalCount: Int? = null,
    val nextPageToken: String? = null,
)
