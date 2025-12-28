package com.example.kinotlin.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import com.example.kinotlin.Titles
import com.example.kinotlin.navigation.Route
import com.example.kinotlin.navigation.TopLevelBackStack
import com.example.kinotlin.titles.data.MockTitlesRepository
import com.example.kinotlin.titles.domain.TitlesRepository
import com.example.kinotlin.titles.presentation.viewModel.TitleDetailsViewModel
import com.example.kinotlin.titles.presentation.viewModel.TitlesListViewModel

val mainModule = module {
    single { TopLevelBackStack<Route>(Titles) }

    single<TitlesRepository> { MockTitlesRepository() }

    viewModel { TitlesListViewModel(get()) }
    viewModel { (titleId: String) -> TitleDetailsViewModel(get(), titleId) }
}