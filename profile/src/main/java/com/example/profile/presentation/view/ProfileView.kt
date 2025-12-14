package com.example.profile.presentation.view


import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.profile.presentation.model.ProfileState
import com.example.profile.presentation.viewModel.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileView() {
    val viewModel = koinViewModel<ProfileViewModel>()
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()

    AccountScreenContent(
        profileState,
        onDownloadResume = viewModel::downloadResume,
        onEditProfile = viewModel::onEditProfile
    )
}

@Composable
private fun AccountScreenContent(
    profileState: ProfileState,
    onDownloadResume: (Context, (Uri) -> Unit) -> Unit,
    onEditProfile: () -> Unit
) {
    AccountScreen(
        profileState, onDownloadResume = onDownloadResume, onEditProfile = onEditProfile
    )

}

@Composable
private fun AccountScreen(
    profileState: ProfileState,
    onDownloadResume: (Context, (Uri) -> Unit) -> Unit,
    onEditProfile: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProfileHeaderWithEditButton(
            photoUri = profileState.photoUri,
            fullName = profileState.fullName,
            time = profileState.time,
            onEditClick = onEditProfile
        )

        ResumeSection(
            profileState = profileState, onDownloadResume = onDownloadResume
        )
    }
}

@Composable
private fun ProfileHeaderWithEditButton(
    photoUri: Uri, fullName: String, time: String = "", onEditClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ProfileImage(
                photoUri = photoUri, contentDescription = "Аватар"
            )

            Text(
                text = fullName.ifEmpty { "Не указано" },
                style = MaterialTheme.typography.headlineMedium
            )

            if (time.isNotEmpty()) {
                Text(
                    text = "Любимая пара: $time",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        IconButton(
            onClick = onEditClick, modifier = Modifier
                .align(Alignment.TopEnd)
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Редактировать профиль",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun ProfileImage(
    photoUri: Uri, contentDescription: String
) {
    val hasImage = photoUri != Uri.EMPTY

    if (hasImage) {
        GlideImageWithPlaceholder(
            imageUri = photoUri, contentDescription = contentDescription
        )
    } else {
        ImagePlaceholder(
            contentDescription = contentDescription
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun GlideImageWithPlaceholder(
    imageUri: Uri, contentDescription: String
) {
    GlideImage(
        model = imageUri,
        contentDescription = contentDescription,
        modifier = Modifier
            .size(128.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun ImagePlaceholder(
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .size(128.dp)
            .clip(CircleShape)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape
            ), contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Person,
            contentDescription = contentDescription,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ResumeSection(
    profileState: ProfileState, onDownloadResume: (Context, (Uri) -> Unit) -> Unit
) {
    val context = LocalContext.current

    if (profileState.resumeUrl.isNotEmpty()) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            if (profileState.isLoadingResume) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Button(
                    onClick = {
                        onDownloadResume(context) { uri ->
                            openPdfFile(context, uri)
                        }
                    }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Скачать резюме")
                }
            }

            profileState.resumeError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private fun openPdfFile(context: Context, uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            context, "Не найдено приложение для просмотра PDF", Toast.LENGTH_LONG
        ).show()
    } catch (e: Exception) {
        Toast.makeText(
            context, "Ошибка при открытии файла", Toast.LENGTH_LONG
        ).show()
    }
}
