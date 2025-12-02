package com.example.f1_app

import android.app.Application
import com.example.f1_app.core.config.di.remoteConfigModule
import com.example.f1_app.di.appModule
import com.example.f1_app.features.auth.di.authModule
import com.example.f1_app.features.home.di.homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule, authModule, remoteConfigModule)
            modules(appModule, authModule, remoteConfigModule, homeModule)
        }
    }
}