package com.example.profile.presentation.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.profile.presentation.receiver.NotificationReceiver
import com.example.profile.presentation.viewModel.EditProfileViewModel
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("DefaultLocale")
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit = {}
) {
    val viewModel: EditProfileViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val timePickerDialog = TimePickerDialog(
        context, { _, hour, minute ->
            val formattedTime = String.format("%02d:%02d", hour, minute)
            viewModel.onTimeChange(formattedTime)
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onPhotoSelected(it) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            state.tempCameraUri?.let { viewModel.onPhotoSelected(it) }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            state.tempCameraUri?.let { uri ->
                cameraLauncher.launch(uri)
            }
        } else {
            viewModel.showPermissionDeniedDialog()
        }
    }

    fun openCamera() {
        val tempFile = File.createTempFile(
            "temp_photo_${System.currentTimeMillis()}", ".jpg", context.cacheDir
        )
        val tempUri = FileProvider.getUriForFile(
            context, "${context.packageName}.fileprovider", tempFile
        )
        viewModel.setTempCameraUri(tempUri)

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            cameraLauncher.launch(tempUri)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Редактирование профиля") }, navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад"
                    )
                }
            }, actions = {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    IconButton(
                        onClick = {
                            if (state.time.isNotEmpty() && !isValidTimeFormat(state.time)) {
                                viewModel.setTimeError(true)
                            } else {
                                if (state.time.isNotEmpty()) {
                                    scheduleNotification(context, state.fullName, state.time)
                                }
                                viewModel.onSaveProfile(onBackClick)
                            }
                        }, enabled = state.fullName.isNotEmpty()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done, contentDescription = "Сохранить"
                        )
                    }
                }
            })
        }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileImageWithPicker(
                photoUri = state.photoUri, onPhotoClick = viewModel::onPhotoClick
            )

            OutlinedTextField(
                value = state.fullName,
                onValueChange = viewModel::onFullNameChange,
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.fullName.isEmpty()
            )

            OutlinedTextField(
                value = state.resumeUrl,
                onValueChange = viewModel::onResumeUrlChange,
                label = { Text("URL резюме") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = state.time,
                onValueChange = viewModel::onTimeChange,
                label = { Text("Время любимой пары") },
                trailingIcon = {
                    IconButton(onClick = { timePickerDialog.show() }) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Выбрать время"
                        )
                    }
                },
                placeholder = { Text("ЧЧ:ММ") },
                singleLine = true,
                isError = state.timeError,
                supportingText = {
                    if (state.timeError) {
                        Text(
                            "Введите время в формате ЧЧ:ММ (например, 14:30)",
                            color = MaterialTheme.colorScheme.error
                        )
                    } else {
                        Text("Формат: ЧЧ:ММ")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            state.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            ImageSourceDialog(
                showDialog = state.showImageSourceDialog,
                onDismiss = viewModel::dismissImageSourceDialog,
                onGalleryClick = {
                    galleryLauncher.launch("image/*")
                },
                onCameraClick = {
                    viewModel.dismissImageSourceDialog()
                    openCamera()
                })

            PermissionDeniedDialog(
                showDialog = state.showPermissionDeniedDialog,
                onDismiss = viewModel::dismissPermissionDeniedDialog
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ProfileImageWithPicker(
    photoUri: Uri, onPhotoClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(128.dp)
            .clip(CircleShape)
            .clickable(onClick = onPhotoClick),
        contentAlignment = Alignment.Center
    ) {
        if (photoUri != Uri.EMPTY) {
            GlideImage(
                model = photoUri,
                contentDescription = "Аватар",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            ImagePlaceholder()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.Black.copy(alpha = 0.3f), shape = CircleShape
                ), contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Изменить фото",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun ImagePlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape
            ), contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Person,
            contentDescription = "Аватар",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ImageSourceDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    if (showDialog) {
        AlertDialog(onDismissRequest = onDismiss, title = { Text("Выберите источник") }, text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onGalleryClick, modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Галерея")
                }
                Button(
                    onClick = onCameraClick, modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Камера")
                }
            }
        }, confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        })
    }
}

@Composable
private fun PermissionDeniedDialog(
    showDialog: Boolean, onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(onDismissRequest = onDismiss, title = { Text("Разрешение отклонено") }, text = {
            Text("Для выбора фото или съемки необходимо предоставить разрешение.")
        }, confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("ОК")
            }
        })
    }
}

private fun isValidTimeFormat(time: String): Boolean {
    val timePattern = Regex("^([01]\\d|2[0-3]):[0-5]\\d$")
    return timePattern.matches(time)
}

private fun scheduleNotification(context: Context, name: String, time: String) {
    val parts = time.split(":")
    val hour = parts.getOrNull(0)?.toIntOrNull() ?: return
    val minute = parts.getOrNull(1)?.toIntOrNull() ?: return

    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        if (before(Calendar.getInstance())) {
            add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("name", name)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        System.currentTimeMillis().toInt(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (!alarmManager.canScheduleExactAlarms()) {
            val settingsIntent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.parse("package:${context.packageName}")
            }
            context.startActivity(settingsIntent)
            return
        }
    }

    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )

    Toast.makeText(
        context,
        "Уведомление установлено на $time",
        Toast.LENGTH_SHORT
    ).show()
}