package com.example.kinotlin.titles.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinotlin.titles.data.favorites.FavoritesRepository
import com.example.kinotlin.titles.presentation.model.TitleUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<List<TitleUiModel>>(emptyList())
    val state: StateFlow<List<TitleUiModel>> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            favoritesRepository.observeFavorites().collectLatest { list ->
                _state.update { list }
            }
        }
    }
}
