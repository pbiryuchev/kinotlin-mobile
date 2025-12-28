@file:OptIn(com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi::class)

package com.example.kinotlin.titles.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import com.example.kinotlin.TitleDetails
import com.example.kinotlin.navigation.Route
import com.example.kinotlin.navigation.TopLevelBackStack
import com.example.kinotlin.titles.presentation.components.TitleListItem
import com.example.kinotlin.titles.presentation.viewModel.FavoritesViewModel

@Composable
fun FavoritesScreen(
    topLevelBackStack: TopLevelBackStack<Route>,
) {
    val viewModel = koinViewModel<FavoritesViewModel>()
    val items by viewModel.state.collectAsStateWithLifecycle()

    Scaffold (contentWindowInsets = WindowInsets(0.dp)) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (items.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = "Пока нет избранного", style = MaterialTheme.typography.headlineSmall)
                    Text(text = "Добавь фильм или сериал на экране деталей", style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(
                        items = items,
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
