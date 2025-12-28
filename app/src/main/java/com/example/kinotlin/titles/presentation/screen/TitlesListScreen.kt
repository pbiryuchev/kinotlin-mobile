@file:OptIn(com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi::class)

package com.example.kinotlin.titles.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import com.example.kinotlin.TitleDetails
import com.example.kinotlin.TitlesFilterSettings
import com.example.kinotlin.navigation.TopLevelBackStack
import com.example.kinotlin.navigation.Route
import com.example.kinotlin.titles.data.badge.TitlesFiltersBadgeCache
import com.example.kinotlin.titles.presentation.components.TitlesFilterOptions
import com.example.kinotlin.titles.presentation.components.TitleListItem
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
    val badgeCache = koinInject<TitlesFiltersBadgeCache>()
    val state by viewModel.viewState.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            BadgedBox(
                badge = {
                    if (badgeCache.shouldShowBadge) {
                        Badge()
                    }
                }
            ) {
                FloatingActionButton(onClick = { topLevelBackStack.add(TitlesFilterSettings) }) {
                    Icon(Icons.Default.Tune, contentDescription = "Фильтры")
                }
            }
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
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
                        item(key = "filters_summary") {
                            val selectedType = state.filter.types?.firstOrNull() ?: TitlesFilterOptions.TYPE_MOVIE
                            val selectedSortBy = state.filter.sortBy ?: TitlesFilterOptions.SORT_BY_POPULARITY
                            val minRating = state.filter.minAggregateRating
                            Text(
                                text = "${TitlesFilterOptions.typeLabel(selectedType)} • ${TitlesFilterOptions.sortLabel(selectedSortBy)} • ${TitlesFilterOptions.minRatingLabel(minRating)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
    }
}
