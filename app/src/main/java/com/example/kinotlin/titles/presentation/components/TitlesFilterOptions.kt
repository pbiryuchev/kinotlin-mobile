package com.example.kinotlin.titles.presentation.components

data class FilterOption<T>(
    val value: T,
    val label: String,
)

object TitlesFilterOptions {

    const val TYPE_MOVIE = "MOVIE"
    const val TYPE_TV_SERIES = "TV_SERIES"

    const val SORT_BY_POPULARITY = "SORT_BY_POPULARITY"
    const val SORT_BY_USER_RATING = "SORT_BY_USER_RATING"

    val typeOptions: List<FilterOption<String>> = listOf(
        FilterOption(TYPE_MOVIE, "Фильмы"),
        FilterOption(TYPE_TV_SERIES, "Сериалы"),
    )

    val sortOptions: List<FilterOption<String>> = listOf(
        FilterOption(SORT_BY_POPULARITY, "Популярность"),
        FilterOption(SORT_BY_USER_RATING, "Рейтинг"),
    )

    val minRatingOptions: List<FilterOption<Float?>> = listOf(
        FilterOption(null, "Любой"),
        FilterOption(6.0f, "6+"),
        FilterOption(7.0f, "7+"),
        FilterOption(8.0f, "8+"),
        FilterOption(9.0f, "9+"),
    )

    fun typeLabel(value: String): String =
        typeOptions.firstOrNull { it.value == value }?.label ?: "Фильмы"

    fun sortLabel(value: String): String =
        sortOptions.firstOrNull { it.value == value }?.label ?: "Популярность"

    fun minRatingLabel(value: Float?): String =
        minRatingOptions.firstOrNull { it.value == value }?.label ?: "Любой"
}
