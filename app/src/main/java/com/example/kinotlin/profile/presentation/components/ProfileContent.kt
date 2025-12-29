package com.example.kinotlin.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.kinotlin.profile.presentation.model.ProfileUiModel

@Composable
fun ProfileContent(
    profile: ProfileUiModel,
    onResumeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(PaddingValues(16.dp)),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ProfileAvatar(avatarUri = profile.avatarUri)
        ProfileName(fullName = profile.fullName)
        ProfilePosition(position = profile.position)

        ResumeButton(
            isEnabled = profile.resumeUrl.isNotBlank(),
            onClick = onResumeClick,
        )
    }
}

@Composable
private fun ProfileAvatar(
    avatarUri: String?,
    modifier: Modifier = Modifier,
) {
    val avatarModifier = Modifier
        .size(140.dp)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)

    if (avatarUri.isNullOrBlank()) {
        DefaultAvatar(modifier = modifier.then(avatarModifier))
    } else {
        UserAvatar(avatarUri = avatarUri, modifier = modifier.then(avatarModifier))
    }
}

@Composable
private fun DefaultAvatar(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Аватар по умолчанию",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(64.dp),
        )
    }
}

@Composable
private fun UserAvatar(
    avatarUri: String,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = avatarUri,
        contentDescription = "Аватар пользователя",
        contentScale = ContentScale.Crop,
        modifier = modifier,
    )
}

@Composable
private fun ProfileName(
    fullName: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = fullName.ifBlank { "Имя не задано" },
        style = MaterialTheme.typography.headlineSmall,
        modifier = modifier,
    )
}

@Composable
private fun ProfilePosition(
    position: String,
    modifier: Modifier = Modifier,
) {
    if (position.isNotBlank()) {
        Text(
            text = position,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier,
        )
    }
}

@Composable
private fun ResumeButton(
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = onClick,
            enabled = isEnabled,
        ) {
            Text(text = "Резюме")
        }
    }
}
