package com.example.kinotlin.profile

import com.example.kinotlin.profile.data.datastore.ProfileStore
import com.example.kinotlin.profile.data.repository.LocalProfileRepository
import com.example.kinotlin.profile.data.resume.AndroidResumeDownloader
import com.example.kinotlin.profile.domain.DownloadResumeUseCase
import com.example.kinotlin.profile.domain.GetProfileUseCase
import com.example.kinotlin.profile.domain.ProfileRepository
import com.example.kinotlin.profile.domain.ResumeDownloader
import com.example.kinotlin.profile.domain.SaveProfileUseCase
import com.example.kinotlin.profile.presentation.viewModel.EditProfileViewModel
import com.example.kinotlin.profile.presentation.viewModel.ProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    single { ProfileStore(get()) }
    single<ProfileRepository> { LocalProfileRepository(get()) }
    single<ResumeDownloader> { AndroidResumeDownloader(androidContext()) }

    factory { GetProfileUseCase(get()) }
    factory { SaveProfileUseCase(get()) }
    factory { DownloadResumeUseCase(get()) }

    viewModel { ProfileViewModel(get(), get()) }
    viewModel { EditProfileViewModel(get(), get()) }
}
