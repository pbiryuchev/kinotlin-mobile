package com.example.kinotlin.titles.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinotlin.titles.domain.GetTitlesUseCase
import com.example.kinotlin.titles.domain.TitlesFilter
import com.example.kinotlin.titles.presentation.model.TitlesListViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TitlesListViewModel(
    private val getTitles: GetTitlesUseCase,
) : ViewModel() {

    private val _viewState = MutableStateFlow(TitlesListViewState())
    val viewState: StateFlow<TitlesListViewState> = _viewState.asStateFlow()

    init {
        load()
    }

    fun onRetry() = load()

    fun onTypeSelected(type: String) {
        val current = _viewState.value.filter
        val updated = current.copy(types = listOf(type))
        _viewState.update { it.copy(filter = updated) }
        load(updated)
    }

    fun onSortBySelected(sortBy: String) {
        val current = _viewState.value.filter
        val updated = current.copy(sortBy = sortBy)
        _viewState.update { it.copy(filter = updated) }
        load(updated)
    }

    fun onMinRatingChanged(minRating: Float?) {
        val current = _viewState.value.filter
        val updated = current.copy(minAggregateRating = minRating)
        _viewState.update { it.copy(filter = updated) }
    }

    fun onMinRatingChangeFinished() {
        load(_viewState.value.filter)
    }

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
