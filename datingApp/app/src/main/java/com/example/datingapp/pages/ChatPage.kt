package com.example.datingapp.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.datingapp.R
import com.example.datingapp.data.Message
import com.example.datingapp.data.SampleData
import com.example.datingapp.ui.theme.LightGray
import com.example.datingapp.ui.theme.Pink
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun ChatPage(userName: String, onBack: () -> Unit) {

    val messages = remember { mutableStateListOf(*SampleData.messages.toTypedArray()) }

    val textState = remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = { ChatTopBar(userName = userName, onBack = onBack) },
        bottomBar = {
            MessageInput(
                message = textState.value,
                onValueChange = { textState.value = it },
                onSend = {
                    if (textState.value.isNotBlank()) {
                        messages.add(
                            Message(
                                text = textState.value,
                                timestamp = LocalDateTime.now(),
                                isSentByUser = true
                            )
                        )
                        textState.value = ""
                        coroutineScope.launch {
                            listState.animateScrollToItem(messages.size - 1)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        MessageList(
            messages = messages,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            listState = listState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(userName: String, onBack: () -> Unit) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Image(
                    imageVector = Icons.Default.Person,
                    contentDescription = "$userName's profile picture",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = userName, fontWeight = FontWeight.Bold)
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = Pink
                )
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(painterResource(R.drawable.ic_menu), contentDescription = "More options")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
    )
}

@Composable
fun MessageList(
    messages: List<Message>,
    modifier: Modifier = Modifier,
    listState: LazyListState
) {
    LazyColumn(
        state = listState,
        modifier = modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        itemsIndexed(messages) { index, message ->
            val prevMessage = messages.getOrNull(index - 1)
            val showTimestampHeader = prevMessage == null ||
                    ChronoUnit.HOURS.between(prevMessage.timestamp, message.timestamp) >= 1

            val isSameUserAsPrev = prevMessage?.isSentByUser == message.isSentByUser
            val isWithin20Seconds = prevMessage != null &&
                    ChronoUnit.SECONDS.between(prevMessage.timestamp, message.timestamp) < 20

            val addExtraSpacing = !(isSameUserAsPrev && isWithin20Seconds)

            if (showTimestampHeader) {
                TimestampHeader(timestamp = message.timestamp)
            }

            MessageBubble(
                message = message,
                modifier = Modifier.padding(top = if (addExtraSpacing) 8.dp else 2.dp)
            )
        }
    }
}

@Composable
fun TimestampHeader(timestamp: LocalDateTime) {
    val formatter = DateTimeFormatter.ofPattern("EEEE h:mm a")
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = timestamp.format(formatter), fontSize = 12.sp, color = Gray)
    }
}

@Composable
fun MessageBubble(message: Message, modifier: Modifier = Modifier) {

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = if (message.isSentByUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(if (message.isSentByUser) Pink else LightGray)
                .widthIn(max = 280.dp),
        ) {
            Text(
                text = message.text,
                color = if (message.isSentByUser) White else Black,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 10.dp
                )
            )
        }
    }
}

@Composable
fun MessageInput(
    message: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Surface(shadowElevation = 8.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 12.dp, bottom = 48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Hey, Sara looks great") },
                shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Pink,
                    unfocusedBorderColor = LightGray
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onSend,
                modifier = Modifier
                    .size(48.dp)
                    .background(Pink, CircleShape)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = White
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Preview(showSystemUi = true)
@Composable
fun ChatPagePreview() {
    ChatPage(userName = "Sarah", onBack = {})
}
