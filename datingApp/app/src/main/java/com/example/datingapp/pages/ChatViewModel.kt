package com.example.datingapp.pages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datingapp.data.Message
import com.example.datingapp.data.MessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ChatViewModel(private val repository: MessageRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        observeMessages()
    }

    private fun observeMessages() {
        viewModelScope.launch {
            repository.allMessages
                .onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }
                .catch { throwable ->
                    _uiState.update {
                        it.copy(isLoading = false, error = throwable.localizedMessage)
                    }
                }
                .collect { messages ->
                    _uiState.update {
                        it.copy(isLoading = false, messages = messages)
                    }
                }
        }
    }

    fun sendMessage(
        text: String,
        isSentByUser: Boolean = true,
    ) {
        val trimmedText = text.trim()
        if (trimmedText.isNotBlank()) {
            val message =
                Message(
                    text = trimmedText,
                    timestamp = LocalDateTime.now(),
                    isSentByUser = isSentByUser,
                )
            viewModelScope.launch {
                try {
                    repository.insert(message)
                } catch (_: Exception) {
                    _uiState.update { it.copy(error = "Failed to send message.") }
                }
            }
        }
    }
}

data class ChatUiState(
    val isLoading: Boolean = true,
    val messages: List<Message> = emptyList(),
    val error: String? = null,
)
