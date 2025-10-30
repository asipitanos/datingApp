package com.example.datingapp.data


import java.time.LocalDateTime


data class Message(
    val text: String,
    val timestamp: LocalDateTime,
    val isSentByUser: Boolean
)

object SampleData {
    val contactNames = listOf("Sarah")

    val messages = listOf(
        Message(
            "Wowsers sounds fun",
            LocalDateTime.now().minusHours(2),
            isSentByUser = false
        ),
        Message(
            "Yeh for sure that works. What time do you think?",
            LocalDateTime.now().minusHours(1).minusMinutes(1),
            isSentByUser = false
        ),
        Message(
            "Does 7pm work for you? I've got to go pick up my little brother first from a party",
            LocalDateTime.now().minusMinutes(5),
            isSentByUser = true
        ),
        Message(
            "Ok cool!",
            LocalDateTime.now().minusMinutes(4),
            isSentByUser = false
        ),
        Message(
            "What are you up to today?",
            LocalDateTime.now().minusMinutes(3),
            isSentByUser = true
        ),
        Message(
            "Nothing much",
            LocalDateTime.now().minusSeconds(90),
            isSentByUser = false
        ),
        Message(
            "Actually just about to go shopping, got any recommendations for a good shoe shop? I'm a fashion disaster",
            LocalDateTime.now().minusSeconds(75),
            isSentByUser = false
        ),
        Message(
            "The last one went on for hours",
            LocalDateTime.now().minusSeconds(60),
            isSentByUser = false
        )
    )
}
