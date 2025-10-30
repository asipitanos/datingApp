package com.example.datingapp.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MessageRepository(private val messageDao: MessageDao) {
    val allMessages: Flow<List<Message>> =
        messageDao.getAllMessages().map { entityList ->
            entityList.map { it.toMessage() }
        }

    suspend fun insert(message: Message) {
        messageDao.insertMessage(message.toEntity())
    }
}
