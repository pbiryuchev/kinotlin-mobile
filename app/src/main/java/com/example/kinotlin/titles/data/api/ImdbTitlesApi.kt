package com.example.kinotlin.titles.data.api

import com.example.kinotlin.titles.data.dto.ListTitlesResponseDto
import com.example.kinotlin.titles.data.dto.TitleDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ImdbTitlesApi {

    @GET("titles")
    suspend fun listTitles(
        @Query("types") types: List<String>? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("sortOrder") sortOrder: String? = null,
        @Query("minAggregateRating") minAggregateRating: Float? = null,
        @Query("pageToken") pageToken: String? = null,
    ): ListTitlesResponseDto

    @GET("titles/{titleId}")
    suspend fun getTitle(
        @Path("titleId") titleId: String,
    ): TitleDto
}
