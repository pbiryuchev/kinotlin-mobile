package com.example.kinotlin.profile.presentation.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinotlin.profile.domain.DownloadResumeUseCase
import com.example.kinotlin.profile.domain.GetProfileUseCase
import com.example.kinotlin.profile.presentation.model.ProfileViewState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfile: GetProfileUseCase,
    private val downloadResume: DownloadResumeUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileViewState())
    val state: StateFlow<ProfileViewState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            getProfile().collectLatest { profile ->
                _state.update { it.copy(profile = profile) }
            }
        }
    }

    fun onResumeClick() {
        val url = state.value.profile.resumeUrl.trim()
        if (url.isBlank()) return

        viewModelScope.launch {
            _events.emit(Event.ShowMessage("Скачивание началось…"))
            val uri = runCatching { downloadResume(url) }.getOrNull()
            if (uri != null) {
                _events.emit(Event.OpenUri(uri))
            } else {
                _events.emit(Event.ShowMessage("Не удалось скачать резюме (проверь ссылку)"))
            }
        }
    }

    sealed interface Event {
        data class OpenUri(val uri: Uri) : Event
        data class ShowMessage(val message: String) : Event
    }
}
