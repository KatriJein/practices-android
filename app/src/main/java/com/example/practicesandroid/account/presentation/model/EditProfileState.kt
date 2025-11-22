package com.example.practicesandroid.account.presentation.model

import android.net.Uri

data class EditProfileState(
    val fullName: String = "",
    val resumeUrl: String = "",
    val photoUri: Uri = Uri.EMPTY,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showImageSourceDialog: Boolean = false,
    val showPermissionDeniedDialog: Boolean = false,
    val tempCameraUri: Uri? = null
)

sealed class EditProfileAction {
    object SaveSuccess : EditProfileAction()
    data class SaveError(val message: String) : EditProfileAction()
}