package com.example.f1_app.di

import com.example.f1_app.features.home.data.repository.HomeRepositoryImpl
import com.example.f1_app.features.home.domain.repository.HomeRepository
import com.example.f1_app.features.home.domain.usecase.GetHomeOverviewUseCase
import com.example.f1_app.features.home.presentation.HomeViewModel
import com.example.f1_app.features.calendar.data.api.OpenF1Service
import com.example.f1_app.features.calendar.data.datasource.CalendarRemoteDataSource
import com.example.f1_app.features.calendar.data.repository.CalendarRepositoryImpl
import com.example.f1_app.features.calendar.domain.repository.CalendarRepository
import com.example.f1_app.features.calendar.domain.usecase.GetMeetingsUseCase
import com.example.f1_app.features.calendar.presentation.CalendarViewModel
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    single<HomeRepository>{ HomeRepositoryImpl() }
    factory { GetHomeOverviewUseCase(get()) }
    viewModel { HomeViewModel(get()) }

    // Networking
    single {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
    single { Moshi.Builder().build() }
    single {
        Retrofit.Builder()
            .baseUrl("https://api.openf1.org/")
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get())
            .build()
    }
    single<OpenF1Service> { get<Retrofit>().create(OpenF1Service::class.java) }

    // Calendar feature
    single { CalendarRemoteDataSource(get()) }
    single<CalendarRepository> { CalendarRepositoryImpl(get()) }
    factory { GetMeetingsUseCase(get()) }
    viewModel { CalendarViewModel(get()) }
}