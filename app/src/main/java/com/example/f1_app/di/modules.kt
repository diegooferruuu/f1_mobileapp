package com.example.f1_app.di

import com.example.f1_app.features.calendar.data.api.OpenF1Service
import com.example.f1_app.features.calendar.data.datasource.CalendarRemoteDataSource
import com.example.f1_app.features.calendar.data.repository.CalendarRepositoryImpl
import com.example.f1_app.features.calendar.domain.repository.CalendarRepository
import com.example.f1_app.features.calendar.domain.usecase.GetMeetingsUseCase
import com.example.f1_app.features.calendar.presentation.CalendarViewModel
import com.example.f1_app.features.drivers.data.api.DriversApiService
import com.example.f1_app.features.drivers.data.datasource.DriverRemoteDataSource
import com.example.f1_app.features.drivers.data.repository.DriverRepositoryImpl
import com.example.f1_app.features.drivers.domain.repository.DriverRepository
import com.example.f1_app.features.drivers.domain.usecase.GetDriversUseCase
import com.example.f1_app.features.drivers.presentation.DriverViewModel
import com.example.f1_app.features.teams.data.api.TeamsApiService
import com.example.f1_app.features.teams.data.datasource.TeamsRemoteDataSource
import com.example.f1_app.features.teams.data.repository.TeamsRepositoryImpl
import com.example.f1_app.features.teams.domain.repository.TeamsRepository
import com.example.f1_app.features.teams.domain.usecase.GetTeamsUseCase
import com.example.f1_app.features.teams.presentation.TeamsViewModel
import com.example.f1_app.features.news.data.api.NewsApiService
import com.example.f1_app.features.news.data.datasource.NewsRemoteDataSource
import com.example.f1_app.features.news.data.repository.NewsRepositoryImpl
import com.example.f1_app.features.news.domain.repository.NewsRepository
import com.example.f1_app.features.news.domain.usecase.GetNewsUseCase
import com.example.f1_app.features.news.presentation.NewsViewModel
import com.example.f1_app.features.results.data.api.ResultsApiService
import com.example.f1_app.features.results.data.datasource.ResultsRemoteDataSource
import com.example.f1_app.features.results.data.repository.ResultsRepositoryImpl
import com.example.f1_app.features.results.domain.repository.ResultsRepository
import com.example.f1_app.features.results.domain.usecase.GetSessionResultsUseCase
import com.example.f1_app.features.results.domain.usecase.SearchSessionsUseCase
import com.example.f1_app.features.results.presentation.ResultsViewModel
import com.example.f1_app.features.results.domain.usecase.GetFastestLapUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    // Home module is now separate - see homeModule

    // Networking
    single {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
    // Moshi with Kotlin support (required for Kotlin data classes)
    single {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl("https://api.openf1.org/")
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get())
            .build()
    }
    single<OpenF1Service> { get<Retrofit>().create(OpenF1Service::class.java) }
    single<ResultsApiService> { get<Retrofit>().create(ResultsApiService::class.java) }

    // Calendar feature
    single { CalendarRemoteDataSource(get()) }
    single<CalendarRepository> { CalendarRepositoryImpl(get()) }
    factory { GetMeetingsUseCase(get()) }
    viewModel { CalendarViewModel(get()) }

    // Drivers Feature
    single<DriversApiService> { get<Retrofit>().create(DriversApiService::class.java) }
    single { DriverRemoteDataSource(get()) }
    single<DriverRepository> { DriverRepositoryImpl(get()) }
    factory { GetDriversUseCase(get()) }
    viewModel { DriverViewModel(get()) }


    // Teams Feature
    single(named("TeamsRetrofit")) {
        Retrofit.Builder()
            .baseUrl("https://v1.formula-1.api-sports.io/")
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get())
            .build()
    }

    single<TeamsApiService> { get<Retrofit>(named("TeamsRetrofit")).create(TeamsApiService::class.java) }
    single { TeamsRemoteDataSource(get()) }
    single<TeamsRepository> { TeamsRepositoryImpl(get()) }
    factory { GetTeamsUseCase(get()) }
    viewModel { TeamsViewModel(get()) }

    // News Feature
    single(named("NewsRetrofit")) {
        Retrofit.Builder()
            .baseUrl("https://f1-latest-news.p.rapidapi.com/")
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get())
            .build()
    }
    single<NewsApiService> { get<Retrofit>(named("NewsRetrofit")).create(NewsApiService::class.java) }
    single { NewsRemoteDataSource(get()) }
    single<NewsRepository> { NewsRepositoryImpl(get()) }
    factory { GetNewsUseCase(get()) }
    viewModel { NewsViewModel(get()) }

    // Results Feature
    single { ResultsRemoteDataSource(get()) }
    single<ResultsRepository> { ResultsRepositoryImpl(get()) }
    factory { SearchSessionsUseCase(get()) }
    factory { GetSessionResultsUseCase(get()) }
    factory { GetFastestLapUseCase(get()) }
    viewModel { ResultsViewModel(get(), get(), get(), get()) }
}