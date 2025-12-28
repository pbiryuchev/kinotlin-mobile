package com.example.kinotlin.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import com.example.kinotlin.Titles
import com.example.kinotlin.navigation.Route
import com.example.kinotlin.navigation.TopLevelBackStack
import retrofit2.Retrofit
import com.example.kinotlin.titles.data.api.ImdbTitlesApi
import com.example.kinotlin.titles.data.mapper.TitleDtoMapper
import com.example.kinotlin.titles.data.repository.NetworkTitlesRepository
import com.example.kinotlin.titles.domain.TitlesRepository
import com.example.kinotlin.titles.domain.GetTitleDetailsUseCase
import com.example.kinotlin.titles.domain.GetTitlesUseCase
import com.example.kinotlin.titles.presentation.viewModel.TitleDetailsViewModel
import com.example.kinotlin.titles.presentation.viewModel.TitlesListViewModel

val mainModule = module {
    single { TopLevelBackStack<Route>(Titles) }

    single { get<Retrofit>().create(ImdbTitlesApi::class.java) }
    factory { TitleDtoMapper() }
    single<TitlesRepository> { NetworkTitlesRepository(get(), get()) }

    factory { GetTitlesUseCase(get()) }
    factory { GetTitleDetailsUseCase(get()) }

    viewModel { TitlesListViewModel(get()) }
    viewModel { (titleId: String) -> TitleDetailsViewModel(get(), get(), titleId) }
}