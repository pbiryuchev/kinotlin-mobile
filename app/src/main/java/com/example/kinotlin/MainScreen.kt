package com.example.kinotlin

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import org.koin.java.KoinJavaComponent.inject
import com.example.kinotlin.navigation.Route
import com.example.kinotlin.navigation.TopLevelBackStack
import com.example.kinotlin.profile.ProfileScreen
import com.example.kinotlin.titles.presentation.screen.TitleDetailsScreen
import com.example.kinotlin.titles.presentation.screen.TitlesFilterSettingsDialog
import com.example.kinotlin.titles.presentation.screen.TitlesListScreen

interface TopLevelRoute : Route {
    val icon: ImageVector
}

@Serializable
data object Titles : TopLevelRoute {
    override val icon: ImageVector = Icons.AutoMirrored.Filled.List
}

@Serializable
data object Profile : TopLevelRoute {
    override val icon: ImageVector = Icons.Default.Person
}

@Serializable
data class TitleDetails(val titleId: String) : Route

@Serializable
data object TitlesFilterSettings : Route

@Composable
fun MainScreen() {
    val topLevelBackStack by inject<TopLevelBackStack<Route>>(clazz = TopLevelBackStack::class.java)
    val dialogStrategy = remember { DialogSceneStrategy<Route>() }

    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf(Titles, Profile).forEach { route ->
                    NavigationBarItem(
                        icon = { Icon(route.icon, null) },
                        selected = topLevelBackStack.topLevelKey == route,
                        onClick = { topLevelBackStack.addTopLevel(route) },
                    )
                }
            }
        },
    ) { padding ->
        NavDisplay(
            backStack = topLevelBackStack.backStack,
            onBack = { topLevelBackStack.removeLast() },
            modifier = Modifier.padding(padding),
            sceneStrategy = dialogStrategy,
            entryDecorators = listOf(
                rememberSavedStateNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<Titles> {
                    TitlesListScreen(topLevelBackStack)
                }
                entry<Profile> {
                    ProfileScreen()
                }
                entry<TitleDetails> {
                    TitleDetailsScreen(
                        titleId = it.titleId,
                        onBack = { topLevelBackStack.removeLast() },
                    )
                }
                entry<TitlesFilterSettings> {
                    with(DialogSceneStrategy.dialog(DialogProperties())) {
                        TitlesFilterSettingsDialog()
                    }
                }
            },
        )
    }
}