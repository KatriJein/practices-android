package com.example.practicesandroid.account.presentation.model

import android.net.Uri

data class ProfileState(
    val fullName: String = "",
    val photoUri: Uri = Uri.EMPTY,
    val resumeUrl: String = "",
    val isLoadingResume: Boolean = false,
    val resumeError: String? = null,
    val time: String = ""
)