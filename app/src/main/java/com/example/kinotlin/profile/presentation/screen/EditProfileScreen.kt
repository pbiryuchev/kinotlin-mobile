package com.example.kinotlin.profile.presentation.screen

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import com.example.kinotlin.profile.presentation.viewModel.EditProfileViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<EditProfileViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var showSourceDialog by remember { mutableStateOf(false) }
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { result ->
            val granted = result.values.all { it }
            if (!granted) onBack()
        },
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(requiredStoragePermissions())
    }

    val pickFromGallery = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            viewModel.onAvatarSelected(uri?.toString())
        },
    )

    val takePicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                viewModel.onAvatarSelected(pendingCameraUri?.toString())
            }
        },
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (!granted) return@rememberLauncherForActivityResult

            val outputUri = createTempImageUri(context)
            pendingCameraUri = outputUri
            takePicture.launch(outputUri)
        },
    )

    if (showSourceDialog) {
        AlertDialog(
            onDismissRequest = { showSourceDialog = false },
            title = { Text(text = "Выбор фото") },
            text = { Text(text = "Откуда взять новую аватарку?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSourceDialog = false
                        pickFromGallery.launch("image/*")
                    },
                ) {
                    Text(text = "Галерея")
                }
            },
            dismissButton = {
                Row {
                    if (!state.avatarUri.isNullOrBlank()) {
                        TextButton(
                            onClick = {
                                showSourceDialog = false
                                viewModel.onAvatarSelected(null)
                            },
                        ) {
                            Text(text = "Удалить")
                        }
                    }
                    TextButton(
                        onClick = {
                            showSourceDialog = false
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        },
                    ) {
                        Text(text = "Камера")
                    }
                }
            },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Редактирование") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.onDone(onSuccess = onBack)
                        },
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Готово")
                    }
                },
            )
        },
        contentWindowInsets = WindowInsets(0.dp),
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(PaddingValues(16.dp)),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            val avatarModifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                .clickable { showSourceDialog = true }

            if (state.avatarUri.isNullOrBlank()) {
                Box(
                    modifier = avatarModifier,
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Аватар",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(56.dp),
                    )
                }
            } else {
                AsyncImage(
                    model = state.avatarUri,
                    contentDescription = "Аватар",
                    contentScale = ContentScale.Crop,
                    modifier = avatarModifier,
                )
            }

            OutlinedTextField(
                value = state.fullName,
                onValueChange = viewModel::onFullNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "ФИО") },
                singleLine = true,
            )

            OutlinedTextField(
                value = state.position,
                onValueChange = viewModel::onPositionChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Должность") },
                singleLine = true,
            )

            OutlinedTextField(
                value = state.resumeUrl,
                onValueChange = viewModel::onResumeUrlChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Ссылка на резюме (pdf)") },
                singleLine = true,
            )

            Button(
                onClick = { viewModel.onDone(onSuccess = onBack) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Сохранить")
            }
        }
    }
}

private fun requiredStoragePermissions(): Array<String> {
    return if (Build.VERSION.SDK_INT >= 33) {
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}

private fun createTempImageUri(context: Context): Uri {
    val cacheDir = File(context.cacheDir, "camera").apply { mkdirs() }
    val file = File.createTempFile("avatar_", ".jpg", cacheDir)
    return FileProvider.getUriForFile(
        context,
        context.packageName + ".fileprovider",
        file,
    )
}
