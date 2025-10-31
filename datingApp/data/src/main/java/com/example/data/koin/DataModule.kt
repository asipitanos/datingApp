package com.example.data.koin

import com.example.data.AppDatabase
import com.example.data.MessageRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule =
    module {
        single { AppDatabase.getDatabase(androidContext()) }
        single { get<AppDatabase>().messageDao() }
        single { MessageRepository(get()) }
    }
