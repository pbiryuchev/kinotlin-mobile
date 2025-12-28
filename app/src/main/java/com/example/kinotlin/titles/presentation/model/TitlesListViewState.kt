package com.example.kinotlin.titles.presentation.model

import com.example.kinotlin.characters.presentation.model.TitleUiModel
import com.example.kinotlin.titles.domain.TitlesFilter

data class TitlesListViewState(
    val state: State = State.Loading,
    val filter: TitlesFilter = TitlesFilter(
        types = listOf("MOVIE"),
        sortBy = "SORT_BY_POPULARITY",
        sortOrder = "DESC",
        minAggregateRating = 7.0f,
    ),
) {
    sealed interface State {
        data object Loading : State
        data class Error(val message: String) : State
        data class Success(val titles: List<TitleUiModel>) : State
    }
}
