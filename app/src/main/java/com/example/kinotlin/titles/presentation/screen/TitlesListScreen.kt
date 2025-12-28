@file:OptIn(com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi::class)

package com.example.kinotlin.titles.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.GlideImage
import org.koin.androidx.compose.koinViewModel
import com.example.kinotlin.TitleDetails
import com.example.kinotlin.navigation.TopLevelBackStack
import com.example.kinotlin.navigation.Route
import com.example.kinotlin.titles.presentation.components.TitlesFiltersBlock
import com.example.kinotlin.titles.presentation.model.TitleUiModel
import com.example.kinotlin.titles.presentation.model.TitlesListViewState
import com.example.kinotlin.titles.presentation.viewModel.TitlesListViewModel
import com.example.kinotlin.uikit.FullscreenError
import com.example.kinotlin.uikit.FullscreenLoading
@Composable
fun TitlesListScreen(
    topLevelBackStack: TopLevelBackStack<Route>,
) {
    val viewModel = koinViewModel<TitlesListViewModel>()
    val state by viewModel.viewState.collectAsStateWithLifecycle()

    when (val s = state.state) {
        TitlesListViewState.State.Loading -> {
            FullscreenLoading()
        }

        is TitlesListViewState.State.Error -> {
            FullscreenError(message = s.message, onRetry = viewModel::onRetry)
        }

        is TitlesListViewState.State.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item(key = "filters") {
                    TitlesFiltersBlock(
                        modifier = Modifier.fillMaxWidth(),
                        state = state,
                        onTypeSelected = viewModel::onTypeSelected,
                        onSortBySelected = viewModel::onSortBySelected,
                        onMinRatingSelected = {
                            viewModel.onMinRatingChanged(it)
                            viewModel.onMinRatingChangeFinished()
                        },
                    )
                }

                items(
                    items = s.titles,
                    key = { it.id },
                ) { item ->
                    TitleListItem(
                        title = item,
                        onClick = { topLevelBackStack.add(TitleDetails(item.id)) },
                    )
                }
            }
        }
    }
}

@Composable
private fun TitleListItem(
    title: TitleUiModel,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            GlideImage(
                model = title.primaryImage.url,
                contentDescription = title.primaryTitle,
                modifier = Modifier
                    .size(72.dp)
                    .clip(MaterialTheme.shapes.medium),
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = title.primaryTitle,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = "${title.type} • ${title.startYear}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Text(
                    text = "IMDb ${title.rating.aggregateRating} (${title.rating.voteCount})",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Text(
                    text = title.genres.take(3).joinToString(separator = " • "),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
