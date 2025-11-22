package com.example.practicesandroid.account.presentation.viewModel

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicesandroid.account.domain.interactor.ProfileInteractor
import com.example.practicesandroid.account.domain.model.ProfileEntity
import com.example.practicesandroid.account.presentation.model.EditProfileAction
import com.example.practicesandroid.account.presentation.model.EditProfileState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditAccountViewModel(
    private val profileInteractor: ProfileInteractor
) : ViewModel() {

    private val mutableState = MutableStateFlow(EditProfileState())
    val state: StateFlow<EditProfileState> = mutableState.asStateFlow()

    private val _actions = MutableSharedFlow<EditProfileAction>()
    val actions: SharedFlow<EditProfileAction> = _actions.asSharedFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            profileInteractor.observeProfile().collect { profile ->
                mutableState.update {
                    it.copy(
                        fullName = profile.fullName,
                        resumeUrl = profile.resumeUrl,
                        photoUri = if (profile.photoUri.isNotEmpty())
                            profile.photoUri.toUri()
                        else
                            Uri.EMPTY
                    )
                }
            }
        }
    }

    fun onFullNameChange(fullName: String) {
        mutableState.update { it.copy(fullName = fullName) }
    }

    fun onResumeUrlChange(resumeUrl: String) {
        mutableState.update { it.copy(resumeUrl = resumeUrl) }
    }

    fun onPhotoClick() {
        mutableState.update { it.copy(showImageSourceDialog = true) }
    }

    fun onPhotoSelected(uri: Uri) {
        mutableState.update { it.copy(photoUri = uri, showImageSourceDialog = false) }
    }

    fun setTempCameraUri(uri: Uri) {
        mutableState.update { it.copy(tempCameraUri = uri) }
    }

    fun dismissImageSourceDialog() {
        mutableState.update { it.copy(showImageSourceDialog = false) }
    }

    fun dismissPermissionDeniedDialog() {
        mutableState.update { it.copy(showPermissionDeniedDialog = false) }
    }

    fun showPermissionDeniedDialog() {
        mutableState.update { it.copy(showPermissionDeniedDialog = true) }
    }

    fun onSaveProfile(onSuccess: () -> Unit) {
        viewModelScope.launch {
            mutableState.update { it.copy(isLoading = true, error = null) }

            try {
                val currentState = state.value

                profileInteractor.saveProfile(
                    ProfileEntity(
                        fullName = currentState.fullName,
                        resumeUrl = currentState.resumeUrl,
                        photoUri = currentState.photoUri.toString()
                    )
                )

                mutableState.update { it.copy(isLoading = false) }
                _actions.emit(EditProfileAction.SaveSuccess)
                onSuccess()

            } catch (e: Exception) {
                mutableState.update {
                    it.copy(
                        isLoading = false,
                        error = "Ошибка сохранения: ${e.message}"
                    )
                }
                _actions.emit(EditProfileAction.SaveError(e.message ?: "Unknown error"))
            }
        }
    }
}
