package com.example.kinotlin.titles.presentation.components

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
    val selectedType = state.filter.types?.firstOrNull() ?: TitlesFilterOptions.TYPE_MOVIE
    val selectedSortBy = state.filter.sortBy ?: TitlesFilterOptions.SORT_BY_POPULARITY
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
                selectedText = TitlesFilterOptions.typeLabel(selectedType),
                items = TitlesFilterOptions.typeOptions.map { it.value to it.label },
                onSelected = { raw -> onTypeSelected(raw) },
            )

            DropdownField(
                modifier = Modifier.weight(1f),
                label = "Сортировка",
                selectedText = TitlesFilterOptions.sortLabel(selectedSortBy),
                items = TitlesFilterOptions.sortOptions.map { it.value to it.label },
                onSelected = { raw -> onSortBySelected(raw) },
            )
        }

        DropdownField(
            modifier = Modifier.fillMaxWidth(),
            label = "Мин. рейтинг IMDb",
            selectedText = TitlesFilterOptions.minRatingLabel(minRating),
            items = TitlesFilterOptions.minRatingOptions.map { it.value to it.label },
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
