package com.example.f1_app.features.auth.di

import com.example.f1_app.features.auth.data.datasource.AuthRemoteDataSource
import com.example.f1_app.features.auth.data.repository.AuthRepositoryImpl
import com.example.f1_app.features.auth.domain.repository.AuthRepository
import com.example.f1_app.features.auth.domain.usecase.LoginUseCase
import com.example.f1_app.features.auth.domain.usecase.SignUpUseCase
import com.example.f1_app.features.auth.domain.usecase.IsUserLoggedInUseCase
import com.example.f1_app.features.auth.domain.usecase.GetCurrentUserUseCase
import com.example.f1_app.features.auth.framework.FirebaseAuthRemoteDataSource
import com.example.f1_app.features.auth.presentation.LoginViewModel
import com.example.f1_app.features.auth.presentation.SignUpViewModel
import com.example.f1_app.presentation.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    // Firebase Auth - lazy instantiation to avoid errors before Gradle sync
    single { com.google.firebase.auth.FirebaseAuth.getInstance() }

    // Data sources
    single<AuthRemoteDataSource> {
        FirebaseAuthRemoteDataSource(get())
    }

    // Repository
    single<AuthRepository> {
        AuthRepositoryImpl(get())
    }

    // Use cases
    factory {
        LoginUseCase(get())
    }
    factory {
        SignUpUseCase(get())
    }
    factory {
        IsUserLoggedInUseCase(get())
    }
    factory {
        GetCurrentUserUseCase(get())
    }

    // ViewModels
    viewModel {
        LoginViewModel(get())
    }
    viewModel {
        SignUpViewModel(get())
    }
    viewModel {
        MainViewModel(get())
    }
}


