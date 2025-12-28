package com.example.kinotlin.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext
import com.example.kinotlin.Titles
import com.example.kinotlin.navigation.Route
import com.example.kinotlin.navigation.TopLevelBackStack
import retrofit2.Retrofit
import com.example.kinotlin.titles.data.api.ImdbTitlesApi
import com.example.kinotlin.titles.data.badge.TitlesFiltersBadgeCache
import com.example.kinotlin.titles.data.datastore.TitlesFiltersStore
import com.example.kinotlin.titles.data.favorites.FavoritesRepository
import com.example.kinotlin.titles.data.favorites.TitlesDatabase
import com.example.kinotlin.titles.data.mapper.TitleDtoMapper
import com.example.kinotlin.titles.data.repository.NetworkTitlesRepository
import com.example.kinotlin.titles.domain.TitlesRepository
import com.example.kinotlin.titles.domain.GetTitleDetailsUseCase
import com.example.kinotlin.titles.domain.GetTitlesUseCase
import com.example.kinotlin.titles.presentation.viewModel.FavoritesViewModel
import com.example.kinotlin.titles.presentation.viewModel.TitlesFilterSettingsViewModel
import com.example.kinotlin.titles.presentation.viewModel.TitleDetailsViewModel
import com.example.kinotlin.titles.presentation.viewModel.TitlesListViewModel

val mainModule = module {
    single { TopLevelBackStack<Route>(Titles) }

    single<DataStore<Preferences>> { providePreferencesDataStore(androidContext()) }
    single { TitlesFiltersStore(get()) }
    single { TitlesFiltersBadgeCache() }

    single {
        Room.databaseBuilder(
            androidContext(),
            TitlesDatabase::class.java,
            "titles.db",
        ).fallbackToDestructiveMigration()
            .build()
    }
    single { get<TitlesDatabase>().favoritesDao() }
    single { FavoritesRepository(get()) }

    single { get<Retrofit>().create(ImdbTitlesApi::class.java) }
    factory { TitleDtoMapper() }
    single<TitlesRepository> { NetworkTitlesRepository(get(), get()) }

    factory { GetTitlesUseCase(get()) }
    factory { GetTitleDetailsUseCase(get()) }

    viewModel { TitlesListViewModel(get(), get(), get()) }
    viewModel { TitlesFilterSettingsViewModel(get(), get(), get()) }
    viewModel { FavoritesViewModel(get()) }
    viewModel { (titleId: String) -> TitleDetailsViewModel(get(), get(), get(), titleId) }
}

private fun providePreferencesDataStore(context: Context): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create {
        context.preferencesDataStoreFile("settings")
    }
}