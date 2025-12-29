package com.example.kinotlin.titles

import androidx.room.Room
import com.example.kinotlin.titles.data.api.ImdbTitlesApi
import com.example.kinotlin.titles.data.badge.TitlesFiltersBadgeCache
import com.example.kinotlin.titles.data.datastore.TitlesFiltersStore
import com.example.kinotlin.titles.data.favorites.FavoritesRepository
import com.example.kinotlin.titles.data.favorites.TitlesDatabase
import com.example.kinotlin.titles.data.mapper.TitleDtoMapper
import com.example.kinotlin.titles.data.repository.NetworkTitlesRepository
import com.example.kinotlin.titles.domain.GetTitleDetailsUseCase
import com.example.kinotlin.titles.domain.GetTitlesUseCase
import com.example.kinotlin.titles.domain.TitlesRepository
import com.example.kinotlin.titles.presentation.viewModel.FavoritesViewModel
import com.example.kinotlin.titles.presentation.viewModel.TitleDetailsViewModel
import com.example.kinotlin.titles.presentation.viewModel.TitlesFilterSettingsViewModel
import com.example.kinotlin.titles.presentation.viewModel.TitlesListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val titlesModule = module {
    single { TitlesFiltersStore(get()) }
    single { TitlesFiltersBadgeCache() }

    single {
        Room.databaseBuilder(
            androidContext(),
            TitlesDatabase::class.java,
            "titles.db",
        ) .build()
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
