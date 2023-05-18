package com.example.covid19project

import android.app.Application
import com.example.covid19project.di.networkModule
import com.example.covid19project.di.viewModelModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CovidApps: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun initKoin(){
        startKoin {
            androidContext(applicationContext)
            modules(networkModule, viewModelModule)
        }
    }
}