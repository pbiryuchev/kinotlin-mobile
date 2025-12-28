package com.example.kinotlin.titles.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import com.example.kinotlin.titles.presentation.components.TitlesFiltersBlock
import com.example.kinotlin.titles.presentation.model.TitlesListViewState
import com.example.kinotlin.titles.presentation.viewModel.TitlesFilterSettingsViewModel

@Composable
fun TitlesFilterSettingsDialog() {
    val viewModel = koinViewModel<TitlesFilterSettingsViewModel>()
    val filter by viewModel.viewState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Настройки фильтров",
            style = MaterialTheme.typography.headlineSmall,
        )

        TitlesFiltersBlock(
            modifier = Modifier.fillMaxWidth(),
            state = TitlesListViewState(filter = filter),
            onTypeSelected = viewModel::onTypeSelected,
            onSortBySelected = viewModel::onSortBySelected,
            onMinRatingSelected = viewModel::onMinRatingSelected,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(
                onClick = viewModel::onSave,
            ) {
                Text("Готово")
            }

            OutlinedButton(
                onClick = viewModel::onBack,
            ) {
                Text("Отмена")
            }
        }
    }
}
