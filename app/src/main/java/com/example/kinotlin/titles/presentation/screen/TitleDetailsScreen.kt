package com.example.kinotlin.titles.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import kotlin.math.roundToInt
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import com.example.kinotlin.titles.presentation.viewModel.TitleDetailsViewModel
import com.example.kinotlin.titles.presentation.model.TitleDetailsScreenState
import com.example.kinotlin.uikit.FullscreenError
import com.example.kinotlin.uikit.FullscreenLoading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleDetailsScreen(
    titleId: String,
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<TitleDetailsViewModel> { parametersOf(titleId) }
    val screenState by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (val s = screenState.state) {
                            is TitleDetailsScreenState.State.Success -> s.details.title.primaryTitle
                            else -> "Детали"
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    val s = screenState.state
                    if (s is TitleDetailsScreenState.State.Success) {
                        IconButton(onClick = viewModel::onFavoriteToggle) {
                            Icon(
                                imageVector = if (s.details.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Избранное",
                            )
                        }
                    }
                },
            )
        },
        contentWindowInsets = WindowInsets(0.dp),
    ) { padding ->
        when (val s = screenState.state) {
            TitleDetailsScreenState.State.Loading -> {
                FullscreenLoading()
            }

            is TitleDetailsScreenState.State.Error -> {
                FullscreenError(message = s.message, onRetry = viewModel::onRetry)
            }

            is TitleDetailsScreenState.State.Success -> {
                val state = s.details
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                        val (poster, title, meta, genres, rating) = createRefs()

                        val posterUrl = state.title.primaryImage.url
                        val posterModifier = Modifier
                            .constrainAs(poster) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }
                            .fillMaxWidth(0.32f)
                            .aspectRatio(2f / 3f)
                            .clip(MaterialTheme.shapes.large)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.large)

                        if (posterUrl.isBlank()) {
                            Box(
                                modifier = posterModifier,
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ImageNotSupported,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        } else {
                            SubcomposeAsyncImage(
                                model = posterUrl,
                                contentDescription = state.title.primaryTitle,
                                contentScale = ContentScale.Crop,
                                modifier = posterModifier,
                                loading = {
                                    Box(
                                        modifier = Modifier.matchParentSize(),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        CircularProgressIndicator(strokeWidth = 2.dp)
                                    }
                                },
                                error = {
                                    Box(
                                        modifier = Modifier.matchParentSize(),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ImageNotSupported,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                },
                            )
                        }

                        Text(
                            text = state.title.primaryTitle,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.constrainAs(title) {
                                top.linkTo(poster.top)
                                start.linkTo(poster.end, margin = 16.dp)
                                end.linkTo(parent.end)
                                width = androidx.constraintlayout.compose.Dimension.fillToConstraints
                            },
                        )

                        Text(
                            text = "${state.title.type} • ${state.title.startYear} • ${(state.title.runtimeSeconds ?: 0) / 60} мин.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.constrainAs(meta) {
                                top.linkTo(title.bottom, margin = 8.dp)
                                start.linkTo(title.start)
                                end.linkTo(title.end)
                                width = androidx.constraintlayout.compose.Dimension.fillToConstraints
                            },
                        )

                        Text(
                            text = state.title.genres.joinToString(separator = ", "),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.constrainAs(genres) {
                                top.linkTo(meta.bottom, margin = 8.dp)
                                start.linkTo(title.start)
                                end.linkTo(title.end)
                                width = androidx.constraintlayout.compose.Dimension.fillToConstraints
                            },
                        )

                        Text(
                            text = "IMDb ${state.title.rating.aggregateRating} (${state.title.rating.voteCount} голосов)",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.constrainAs(rating) {
                                top.linkTo(genres.bottom, margin = 12.dp)
                                start.linkTo(title.start)
                                end.linkTo(title.end)
                                width = androidx.constraintlayout.compose.Dimension.fillToConstraints
                            },
                        )
                    }

                    Text(
                        text = "Описание",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = state.title.plot.takeIf { it.isNotBlank() } ?: "Описание отсутствует",
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    Text(
                        text = "Ваш рейтинг: ${state.userRating ?: "не задан"}",
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Slider(
                        value = (state.userRating ?: 1).toFloat(),
                        onValueChange = { viewModel.onUserRatingChanged(it.roundToInt()) },
                        valueRange = 1f..10f,
                        steps = 8,
                    )
                }
            }
        }
    }
}
