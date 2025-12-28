package com.example.kinotlin.profile.presentation.screen

import android.content.Intent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import com.example.kinotlin.EditProfile
import com.example.kinotlin.navigation.Route
import com.example.kinotlin.navigation.TopLevelBackStack
import com.example.kinotlin.profile.presentation.components.ProfileContent
import com.example.kinotlin.profile.presentation.viewModel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    topLevelBackStack: TopLevelBackStack<Route>,
) {
    val viewModel = koinViewModel<ProfileViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ProfileViewModel.Event.OpenUri -> {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(event.uri, "application/pdf")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    val started = runCatching {
                        context.startActivity(intent)
                        true
                    }.getOrDefault(false)

                    if (!started) {
                        snackbarHostState.showSnackbar("Не удалось открыть резюме")
                    }
                }

                is ProfileViewModel.Event.ShowMessage -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Профиль") },
                actions = {
                    IconButton(onClick = { topLevelBackStack.add(EditProfile) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Редактировать")
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0.dp),
    ) { padding ->
        ProfileContent(
            profile = state.profile,
            onResumeClick = viewModel::onResumeClick,
            modifier = Modifier.padding(padding),
        )
    }
}
