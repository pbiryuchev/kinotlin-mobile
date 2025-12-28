package com.example.kinotlin.titles.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.kinotlin.characters.presentation.model.TitleDetailsViewState
import com.example.kinotlin.titles.domain.TitlesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TitleDetailsViewModel(
    private val repository: TitlesRepository,
    private val titleId: String,
) : ViewModel() {

    private val title = requireNotNull(repository.getTitleById(titleId)) {
        "Тайтл не найден"
    }

    private val _state = MutableStateFlow(
        TitleDetailsViewState(
            title = title,
            userRating = repository.getUserRating(titleId),
        )
    )
    val state: StateFlow<TitleDetailsViewState> = _state.asStateFlow()

    fun onUserRatingChanged(newRating: Int?) {
        val normalized = newRating?.coerceIn(1, 10)
        repository.setUserRating(titleId, normalized)
        _state.value = _state.value.copy(userRating = normalized)
    }
}
