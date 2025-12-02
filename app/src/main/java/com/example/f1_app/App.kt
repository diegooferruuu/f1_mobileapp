package com.example.f1_app

import android.app.Application
import com.example.f1_app.di.appModule
import com.example.f1_app.features.auth.di.authModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule, authModule)
        }
    }
}