package com.example.f1_app.core.config.di

import com.example.f1_app.core.config.data.RemoteConfigRepositoryImpl
import com.example.f1_app.core.config.domain.RemoteConfigRepository
import com.example.f1_app.core.config.domain.usecase.FetchRemoteConfigUseCase
import com.example.f1_app.core.config.domain.usecase.GetRemoteConfigStringUseCase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.koin.dsl.module

val remoteConfigModule = module {
    // Firebase Remote Config
    single {
        FirebaseRemoteConfig.getInstance()
    }

    // Repository
    single<RemoteConfigRepository> {
        RemoteConfigRepositoryImpl(get())
    }

    // Use Cases
    factory {
        GetRemoteConfigStringUseCase(get())
    }
    factory {
        FetchRemoteConfigUseCase(get())
    }
}

