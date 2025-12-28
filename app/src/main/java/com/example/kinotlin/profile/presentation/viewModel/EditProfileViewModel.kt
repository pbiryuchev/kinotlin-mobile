package com.example.kinotlin.profile.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinotlin.profile.domain.GetProfileUseCase
import com.example.kinotlin.profile.domain.Profile
import com.example.kinotlin.profile.domain.SaveProfileUseCase
import com.example.kinotlin.profile.presentation.model.EditProfileViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val getProfile: GetProfileUseCase,
    private val saveProfile: SaveProfileUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(EditProfileViewState())
    val state: StateFlow<EditProfileViewState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val profile = getProfile().first()
            _state.update {
                it.copy(
                    fullName = profile.fullName,
                    avatarUri = profile.avatarUri,
                    resumeUrl = profile.resumeUrl,
                    position = profile.position,
                )
            }
        }
    }

    fun onFullNameChange(value: String) = _state.update { it.copy(fullName = value) }
    fun onResumeUrlChange(value: String) = _state.update { it.copy(resumeUrl = value) }
    fun onPositionChange(value: String) = _state.update { it.copy(position = value) }
    fun onAvatarSelected(uri: String?) = _state.update { it.copy(avatarUri = uri) }

    fun onDone(onSuccess: () -> Unit) {
        val s = _state.value
        viewModelScope.launch {
            saveProfile(
                Profile(
                    fullName = s.fullName.trim(),
                    avatarUri = s.avatarUri,
                    resumeUrl = s.resumeUrl.trim(),
                    position = s.position.trim(),
                )
            )
            onSuccess()
        }
    }
}
