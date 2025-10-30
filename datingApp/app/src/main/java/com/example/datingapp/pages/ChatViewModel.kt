package com.example.datingapp.pages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datingapp.data.Message
import com.example.datingapp.data.MessageRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ChatViewModel(private val repository: MessageRepository) : ViewModel() {
    val messages: StateFlow<List<Message>> =
        repository.allMessages
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList(),
            )

    fun sendMessage(text: String) {
        if (text.isNotBlank()) {
            val message =
                Message(
                    text = text,
                    timestamp = LocalDateTime.now(),
                    isSentByUser = true,
                )
            viewModelScope.launch {
                repository.insert(message)
            }
        }
    }
}
