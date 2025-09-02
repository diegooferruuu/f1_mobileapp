package com.example.f1_app.di

import com.example.f1_app.features.home.data.repository.HomeRepositoryImpl
import com.example.f1_app.features.home.domain.repository.HomeRepository
import com.example.f1_app.features.home.domain.usecase.GetHomeOverviewUseCase
import com.example.f1_app.features.home.presentation.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<HomeRepository>{ HomeRepositoryImpl() }
    factory { GetHomeOverviewUseCase(get()) }
    viewModel { HomeViewModel(get()) }
}