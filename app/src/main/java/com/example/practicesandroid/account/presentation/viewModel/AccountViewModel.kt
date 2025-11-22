package com.example.practicesandroid.account.presentation.viewModel

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicesandroid.DriverDetails
import com.example.practicesandroid.EditProfile
import com.example.practicesandroid.account.domain.interactor.ProfileInteractor
import com.example.practicesandroid.account.presentation.model.AccountViewState
import com.example.practicesandroid.account.presentation.model.FavoriteDriverUIModel
import com.example.practicesandroid.account.presentation.model.ProfileState
import com.example.practicesandroid.drivers.domain.interactor.DriversInteractor
import com.example.practicesandroid.navigation.Route
import com.example.practicesandroid.navigation.TopLevelBackStack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class AccountViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val interactor: DriversInteractor,
    private val profileInteractor: ProfileInteractor,
) : ViewModel() {
    private val mutableState = MutableStateFlow(AccountViewState())
    val state: StateFlow<AccountViewState> = mutableState.asStateFlow()

    private val mutableProfileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = mutableProfileState.asStateFlow()

    init {
        loadFavorites()
        getProfile()
    }

    fun onEditProfile() {
        topLevelBackStack.add(EditProfile)
    }

    fun onDriverClick(driver: FavoriteDriverUIModel) {
        topLevelBackStack.add(
            DriverDetails(
                driverId = driver.id ?: "",
                initialPoints = driver.points,
                initialPosition = driver.position,
                initialWins = driver.wins
            )
        )
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            try {
                interactor.getFavorites().collect { favorites ->
                    val uiModels = favorites.map { favoriteEntity ->
                        FavoriteDriverUIModel(
                            id = favoriteEntity.id,
                            name = favoriteEntity.name,
                            surname = favoriteEntity.surname,
                            number = favoriteEntity.number,
                            teamName = favoriteEntity.teamName,
                            points = favoriteEntity.points,
                            position = favoriteEntity.position,
                            wins = favoriteEntity.wins,
                            nationality = favoriteEntity.nationality,
                            birthday = favoriteEntity.birthday
                        )
                    }

                    updateState(AccountViewState.State.Success(uiModels))
                }
            } catch (e: Exception) {
                updateState(AccountViewState.State.Error(e.message ?: "Unknown error"))
            }
        }
    }

    private fun getProfile() {
        viewModelScope.launch {
            profileInteractor.observeProfile().collect { profile ->
                mutableProfileState.value = mutableProfileState.value.copy(
                    fullName = profile.fullName,
                    photoUri = if (profile.photoUri.isNotEmpty()) profile.photoUri.toUri() else Uri.EMPTY,
                    resumeUrl = profile.resumeUrl
                )
            }
        }
    }

    fun downloadResume(context: Context, onFileDownloaded: (Uri) -> Unit) {
        val url = mutableProfileState.value.resumeUrl
        if (url.isEmpty()) return

        viewModelScope.launch {
            mutableProfileState.value =
                mutableProfileState.value.copy(isLoadingResume = true, resumeError = null)

            try {
                val fileUri = withContext(Dispatchers.IO) {
                    val urlObj = URL(url)
                    val connection = urlObj.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.connectTimeout = 15000
                    connection.readTimeout = 15000
                    connection.connect()

                    val contentType = connection.contentType
                    if (!contentType.contains("pdf") && !contentType.contains("application")) {
                        throw IOException("Неверный формат файла. Ожидается PDF, получен: $contentType") as Throwable
                    }

                    val contentLength = connection.contentLength
                    if (contentLength <= 0) {
                        throw IOException("Файл пустой или недоступен")
                    }

                    val inputStream: InputStream = connection.inputStream

                    val fileName = getValidFileName(url, "resume.pdf")
                    val file =
                        File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)

                    FileOutputStream(file).use { output ->
                        inputStream.use { input ->
                            input.copyTo(output)
                        }
                    }

                    if (file.length() == 0L) {
                        file.delete()
                        throw IOException("Загруженный файл пустой")
                    }

                    FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        file
                    )
                }

                mutableProfileState.value = mutableProfileState.value.copy(isLoadingResume = false)
                onFileDownloaded(fileUri)

            } catch (e: Exception) {
                mutableProfileState.value = mutableProfileState.value.copy(
                    isLoadingResume = false,
                    resumeError = "Ошибка загрузки: ${e.message ?: "Неизвестная ошибка"}"
                )
            }
        }
    }

    private fun getValidFileName(url: String, defaultName: String): String {
        return try {
            val fileNameFromUrl = url.substringAfterLast("/").takeIf {
                it.isNotEmpty() && it.contains(".")
            } ?: defaultName

            if (!fileNameFromUrl.endsWith(".pdf", ignoreCase = true)) {
                "resume.pdf"
            } else {
                fileNameFromUrl
            }
        } catch (e: Exception) {
            defaultName
        }
    }

    private fun updateState(newState: AccountViewState.State) {
        mutableState.update { it.copy(state = newState) }
    }
}

