package com.example.datingapp.koin

import com.example.datingapp.data.AppDatabase
import com.example.datingapp.data.MessageRepository
import com.example.datingapp.pages.ChatViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule =
    module {

        single { AppDatabase.getDatabase(androidContext()) }
        single { get<AppDatabase>().messageDao() }
        single { MessageRepository(get()) }

        viewModel {
            ChatViewModel(get())
        }
    }
