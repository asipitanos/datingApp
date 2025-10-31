package com.example.datingapp.koin

import com.example.data.MessageRepository
import com.example.datingapp.pages.ChatViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule =
    module {
        viewModel {
            ChatViewModel(get<MessageRepository>())
        }
    }
