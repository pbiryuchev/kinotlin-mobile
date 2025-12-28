package com.example.kinotlin.titles.data.favorites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_titles")
data class FavoriteTitleEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "primary_title")
    val primaryTitle: String,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "start_year")
    val startYear: Int,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @ColumnInfo(name = "image_width")
    val imageWidth: Int,

    @ColumnInfo(name = "image_height")
    val imageHeight: Int,

    @ColumnInfo(name = "rating")
    val rating: Double,

    @ColumnInfo(name = "vote_count")
    val voteCount: Int,
)
