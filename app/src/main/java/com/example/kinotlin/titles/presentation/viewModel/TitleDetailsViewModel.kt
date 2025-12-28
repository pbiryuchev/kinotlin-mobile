package com.example.kinotlin.titles.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinotlin.titles.presentation.model.TitleDetailsViewState
import com.example.kinotlin.titles.domain.GetTitleDetailsUseCase
import com.example.kinotlin.titles.domain.TitlesRepository
import com.example.kinotlin.titles.presentation.model.TitleDetailsScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TitleDetailsViewModel(
    private val getTitleDetails: GetTitleDetailsUseCase,
    private val repository: TitlesRepository,
    private val titleId: String,
) : ViewModel() {

    private val _state = MutableStateFlow(TitleDetailsScreenState())
    val state: StateFlow<TitleDetailsScreenState> = _state.asStateFlow()

    init {
        load()
    }

    fun onUserRatingChanged(newRating: Int?) {
        val normalized = newRating?.coerceIn(1, 10)
        repository.setUserRating(titleId, normalized)

        val current = _state.value.state
        if (current is TitleDetailsScreenState.State.Success) {
            _state.update {
                it.copy(state = TitleDetailsScreenState.State.Success(current.details.copy(userRating = normalized)))
            }
        }
    }

    fun onRetry() = load()

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(state = TitleDetailsScreenState.State.Loading) }

            runCatching {
                getTitleDetails(titleId)
            }.onSuccess { title ->
                _state.update {
                    it.copy(
                        state = TitleDetailsScreenState.State.Success(
                            TitleDetailsViewState(
                                title = title,
                                userRating = repository.getUserRating(titleId),
                            )
                        )
                    )
                }
            }.onFailure { e ->
                _state.update {
                    it.copy(state = TitleDetailsScreenState.State.Error(e.localizedMessage ?: "Ошибка загрузки"))
                }
            }
        }
    }
}
