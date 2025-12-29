package com.example.kinotlin.titles.presentation.model

data class TitleDetailsScreenState(
    val state: State = State.Loading,
) {
    sealed interface State {
        data object Loading : State
        data class Error(val message: String) : State
        data class Success(val details: TitleDetailsViewState) : State
    }
}
