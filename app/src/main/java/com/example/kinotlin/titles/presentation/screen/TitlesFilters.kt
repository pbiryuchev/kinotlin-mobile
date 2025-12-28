package com.example.kinotlin.titles.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kinotlin.titles.presentation.model.TitlesListViewState

@Composable
internal fun TitlesFiltersBlock(
    modifier: Modifier,
    state: TitlesListViewState,
    onTypeSelected: (String) -> Unit,
    onSortBySelected: (String) -> Unit,
    onMinRatingSelected: (Float?) -> Unit,
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
            onSelected = { value -> onMinRatingSelected(value) },
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
