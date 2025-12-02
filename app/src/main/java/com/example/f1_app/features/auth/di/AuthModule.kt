package com.example.f1_app.features.auth.di

import com.example.f1_app.features.auth.data.datasource.AuthRemoteDataSource
import com.example.f1_app.features.auth.data.repository.AuthRepositoryImpl
import com.example.f1_app.features.auth.domain.repository.AuthRepository
import com.example.f1_app.features.auth.domain.usecase.LoginUseCase
import com.example.f1_app.features.auth.framework.FirebaseAuthRemoteDataSource
import com.example.f1_app.features.auth.presentation.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    // Firebase Auth
    single { FirebaseAuth.getInstance() }

    // Data sources
    single<AuthRemoteDataSource> { FirebaseAuthRemoteDataSource(get()) }

    // Repository
    single<AuthRepository> { AuthRepositoryImpl(get()) }

    // Use cases
    factory { LoginUseCase(get()) }

    // ViewModels
    viewModel { LoginViewModel(get()) }
}

