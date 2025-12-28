package com.example.kinotlin.titles.presentation.model

import com.example.kinotlin.characters.presentation.model.TitleDetailsViewState

data class TitleDetailsScreenState(
    val state: State = State.Loading,
) {
    sealed interface State {
        data object Loading : State
        data class Error(val message: String) : State
        data class Success(val details: TitleDetailsViewState) : State
    }
}
