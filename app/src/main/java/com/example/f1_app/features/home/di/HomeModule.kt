package com.example.f1_app.features.home.di

import com.example.f1_app.features.home.data.datasource.HomeRemoteDataSource
import com.example.f1_app.features.home.data.repository.HomeRepositoryImpl
import com.example.f1_app.features.home.domain.repository.HomeRepository
import com.example.f1_app.features.home.domain.usecase.GetHomeOverviewUseCase
import com.example.f1_app.features.home.framework.FirebaseHomeDataSource
import com.example.f1_app.features.home.presentation.HomeViewModel
import com.google.firebase.database.FirebaseDatabase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    // Firebase Database
    single {
        FirebaseDatabase.getInstance()
    }

    // Data sources
    single<HomeRemoteDataSource> {
        FirebaseHomeDataSource(get())
    }

    // Repository
    single<HomeRepository> {
        HomeRepositoryImpl(get())
    }

    // Use cases
    factory {
        GetHomeOverviewUseCase(get())
    }

    // ViewModels
    viewModel {
        HomeViewModel(get(), get(), get())
    }
}

