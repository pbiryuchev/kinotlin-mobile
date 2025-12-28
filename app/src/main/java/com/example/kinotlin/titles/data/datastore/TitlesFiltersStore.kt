package com.example.kinotlin.titles.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.kinotlin.titles.domain.TitlesFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TitlesFiltersStore(
    private val dataStore: DataStore<Preferences>,
) {

    private val typeKey = stringPreferencesKey(KEY_TYPE)
    private val sortByKey = stringPreferencesKey(KEY_SORT_BY)
    private val sortOrderKey = stringPreferencesKey(KEY_SORT_ORDER)
    private val minRatingKey = floatPreferencesKey(KEY_MIN_RATING)

    fun filterFlow(): Flow<TitlesFilter> = dataStore.data.map { prefs ->
        TitlesFilter(
            types = listOf(prefs[typeKey] ?: DEFAULT_TYPE),
            sortBy = prefs[sortByKey] ?: DEFAULT_SORT_BY,
            sortOrder = prefs[sortOrderKey] ?: DEFAULT_SORT_ORDER,
            minAggregateRating = prefs[minRatingKey],
        )
    }

    suspend fun save(filter: TitlesFilter) {
        dataStore.edit { prefs ->
            prefs[typeKey] = filter.types?.firstOrNull() ?: DEFAULT_TYPE
            prefs[sortByKey] = filter.sortBy ?: DEFAULT_SORT_BY
            prefs[sortOrderKey] = filter.sortOrder ?: DEFAULT_SORT_ORDER

            val minRating = filter.minAggregateRating
            if (minRating == null) {
                prefs.remove(minRatingKey)
            } else {
                prefs[minRatingKey] = minRating
            }
        }
    }

    companion object {
        private const val KEY_TYPE = "titles_filter_type"
        private const val KEY_SORT_BY = "titles_filter_sort_by"
        private const val KEY_SORT_ORDER = "titles_filter_sort_order"
        private const val KEY_MIN_RATING = "titles_filter_min_rating"

        private const val DEFAULT_TYPE = "MOVIE"
        private const val DEFAULT_SORT_BY = "SORT_BY_POPULARITY"
        private const val DEFAULT_SORT_ORDER = "DESC"
    }
}
