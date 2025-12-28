package com.example.kinotlin.titles.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinotlin.navigation.Route
import com.example.kinotlin.navigation.TopLevelBackStack
import com.example.kinotlin.titles.data.badge.TitlesFiltersBadgeCache
import com.example.kinotlin.titles.data.datastore.TitlesFiltersStore
import com.example.kinotlin.titles.domain.TitlesFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TitlesFilterSettingsViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val store: TitlesFiltersStore,
    private val badgeCache: TitlesFiltersBadgeCache,
) : ViewModel() {

    private val _viewState = MutableStateFlow(TitlesFilter())
    val viewState: StateFlow<TitlesFilter> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            val saved = store.filterFlow().first()
            _viewState.update { saved }
        }
    }

    fun onTypeSelected(type: String) {
        _viewState.update { it.copy(types = listOf(type)) }
    }

    fun onSortBySelected(sortBy: String) {
        _viewState.update { it.copy(sortBy = sortBy) }
    }

    fun onMinRatingSelected(minRating: Float?) {
        _viewState.update { it.copy(minAggregateRating = minRating) }
    }

    fun onSave() {
        val filter = _viewState.value
        viewModelScope.launch {
            store.save(filter)
            badgeCache.update(filter)
            onBack()
        }
    }

    fun onBack() {
        topLevelBackStack.removeLast()
    }
}
