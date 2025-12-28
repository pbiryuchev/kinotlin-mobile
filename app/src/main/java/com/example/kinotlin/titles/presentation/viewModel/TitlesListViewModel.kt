package com.example.kinotlin.titles.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.kinotlin.characters.presentation.model.TitleUiModel
import com.example.kinotlin.titles.domain.TitlesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TitlesListViewModel(
    private val repository: TitlesRepository,
) : ViewModel() {

    private val _titles = MutableStateFlow<List<TitleUiModel>>(emptyList())
    val titles: StateFlow<List<TitleUiModel>> = _titles.asStateFlow()

    init {
        _titles.value = repository.getTitles()
    }
}
