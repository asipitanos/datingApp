package com.example.datingapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val timestamp: Long,
    val isSentByUser: Boolean
)

fun MessageEntity.toMessage(): Message {
    return Message(
        text = this.text,
        timestamp = LocalDateTime.ofEpochSecond(this.timestamp, 0, java.time.ZoneOffset.UTC),
        isSentByUser = this.isSentByUser
    )
}

fun Message.toEntity(): MessageEntity {
    return MessageEntity(
        text = this.text,
        timestamp = this.timestamp.toEpochSecond(java.time.ZoneOffset.UTC),
        isSentByUser = this.isSentByUser
    )
}
