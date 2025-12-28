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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.kinotlin.characters.presentation.model.TitleUiModel
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
                    FiltersBlock(
                        modifier = Modifier.fillMaxWidth(),
                        state = state,
                        onTypeSelected = viewModel::onTypeSelected,
                        onSortBySelected = viewModel::onSortBySelected,
                        onMinRatingChanged = viewModel::onMinRatingChanged,
                        onMinRatingChangeFinished = viewModel::onMinRatingChangeFinished,
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
private fun FiltersBlock(
    modifier: Modifier,
    state: TitlesListViewState,
    onTypeSelected: (String) -> Unit,
    onSortBySelected: (String) -> Unit,
    onMinRatingChanged: (Float?) -> Unit,
    onMinRatingChangeFinished: () -> Unit,
) {
    val selectedType = state.filter.types?.firstOrNull() ?: "MOVIE"
    val selectedSortBy = state.filter.sortBy ?: "SORT_BY_POPULARITY"
    val minRating = state.filter.minAggregateRating

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(text = "Фильтры", style = MaterialTheme.typography.titleMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            DropdownField(
                modifier = Modifier.weight(1f),
                label = "Тип",
                selectedText = when (selectedType) {
                    "TV_SERIES" -> "Сериалы"
                    else -> "Фильмы"
                },
                items = listOf(
                    "MOVIE" to "Фильмы",
                    "TV_SERIES" to "Сериалы",
                ),
                onSelected = { raw -> onTypeSelected(raw) },
            )

            DropdownField(
                modifier = Modifier.weight(1f),
                label = "Сортировка",
                selectedText = when (selectedSortBy) {
                    "SORT_BY_USER_RATING" -> "Рейтинг"
                    else -> "Популярность"
                },
                items = listOf(
                    "SORT_BY_POPULARITY" to "Популярность",
                    "SORT_BY_USER_RATING" to "Рейтинг",
                ),
                onSelected = { raw -> onSortBySelected(raw) },
            )
        }

        val ratingText = when (minRating) {
            null -> "Любой"
            6.0f -> "6+"
            7.0f -> "7+"
            8.0f -> "8+"
            9.0f -> "9+"
            else -> "${minRating}+"
        }
        DropdownField(
            modifier = Modifier.fillMaxWidth(),
            label = "Мин. рейтинг IMDb",
            selectedText = ratingText,
            items = listOf(
                null to "Любой",
                6.0f to "6+",
                7.0f to "7+",
                8.0f to "8+",
                9.0f to "9+",
            ),
            onSelected = { value ->
                onMinRatingChanged(value)
                onMinRatingChangeFinished()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> DropdownField(
    modifier: Modifier,
    label: String,
    selectedText: String,
    items: List<Pair<T, String>>,
    onSelected: (T) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                .fillMaxWidth(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEach { (value, text) ->
                DropdownMenuItem(
                    text = { Text(text = text) },
                    onClick = {
                        expanded = false
                        onSelected(value)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
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
