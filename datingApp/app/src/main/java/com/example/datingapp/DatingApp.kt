package com.example.datingapp

import android.app.Application
import com.example.datingapp.koin.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class DatingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DatingApp)
            modules(appModule)
        }
    }
}
