package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.ZoneOffset

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val timestamp: Long,
    val isSentByUser: Boolean,
)

fun MessageEntity.toMessage(): Message {
    return Message(
        text = this.text,
        timestamp = LocalDateTime.ofEpochSecond(this.timestamp, 0, ZoneOffset.UTC),
        isSentByUser = this.isSentByUser,
    )
}

fun Message.toEntity(): MessageEntity {
    return MessageEntity(
        text = this.text,
        timestamp = this.timestamp.toEpochSecond(ZoneOffset.UTC),
        isSentByUser = this.isSentByUser,
    )
}
