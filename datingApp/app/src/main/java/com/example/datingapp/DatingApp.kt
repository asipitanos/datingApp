package com.example.datingapp

import android.app.Application
import com.example.data.koin.dataModule
import com.example.datingapp.koin.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class DatingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            printLogger(Level.DEBUG)
            androidContext(this@DatingApp)
            modules(dataModule, appModule)
        }
    }
}
