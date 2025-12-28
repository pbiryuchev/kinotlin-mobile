package com.example.kinotlin.titles.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinotlin.titles.data.badge.TitlesFiltersBadgeCache
import com.example.kinotlin.titles.data.datastore.TitlesFiltersStore
import com.example.kinotlin.titles.domain.GetTitlesUseCase
import com.example.kinotlin.titles.domain.TitlesFilter
import com.example.kinotlin.titles.presentation.model.TitlesListViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TitlesListViewModel(
    private val getTitles: GetTitlesUseCase,
    private val filtersStore: TitlesFiltersStore,
    private val badgeCache: TitlesFiltersBadgeCache,
) : ViewModel() {

    private val _viewState = MutableStateFlow(TitlesListViewState())
    val viewState: StateFlow<TitlesListViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            filtersStore.filterFlow().collectLatest { filter ->
                badgeCache.update(filter)
                _viewState.update { it.copy(filter = filter) }
                load(filter)
            }
        }
    }

    fun onRetry() = load(_viewState.value.filter)

    private fun load(filter: TitlesFilter = _viewState.value.filter) {
        viewModelScope.launch {
            _viewState.update { it.copy(state = TitlesListViewState.State.Loading) }
            runCatching {
                getTitles(filter)
            }.onSuccess { titles ->
                _viewState.update { it.copy(state = TitlesListViewState.State.Success(titles)) }
            }.onFailure { e ->
                _viewState.update {
                    it.copy(state = TitlesListViewState.State.Error(e.localizedMessage ?: "Ошибка загрузки"))
                }
            }
        }
    }
}
