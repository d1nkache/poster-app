package com.example.poster.presentation.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PosterBackground = Color(0xFF030306)
private val PosterTopBar = Color(0xFF101014)
private val PosterSurface = Color(0xFF1B1A31)
private val PosterStroke = Color(0xFF302B68)
private val PosterPrimary = Color(0xFF625BFF)
private val PosterPrimaryDark = Color(0xFF493CCB)
private val PosterTextPrimary = Color(0xFFF7F7FF)
private val PosterTextSecondary = Color(0xFFA5A6BA)
private val PosterTextMuted = Color(0xFF7B7D92)
private val PosterDivider = Color(0xFF1B1A25)

data class MessageUi(
    val id: String,
    val text: String,
    val time: String,
    val isMine: Boolean,
)

val sampleMessages = listOf(
    MessageUi(
        id = "1",
        text = "Hey! How are you?",
        time = "10:00 AM",
        isMine = false,
    ),
    MessageUi(
        id = "2",
        text = "I'm good, thanks! Just finished the project.",
        time = "10:05 AM",
        isMine = true,
    ),
    MessageUi(
        id = "3",
        text = "That's great! Can you send me the files?",
        time = "10:06 AM",
        isMine = false,
    ),
    MessageUi(
        id = "4",
        text = "Sure, I'll send them in a few minutes.",
        time = "10:08 AM",
        isMine = true,
    ),
    MessageUi(
        id = "5",
        text = "Thanks! Looking forward to it.",
        time = "10:10 AM",
        isMine = false,
    ),
)

@Composable
fun MessagesScreen(
    contactName: String = "Alice Johnson",
    contactInitials: String = "AJ",
    messages: List<MessageUi> = sampleMessages,
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
    onSendClick: (String) -> Unit = {},
) {
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val lastMessageId = messages.lastOrNull()?.id

    LaunchedEffect(lastMessageId) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF08051A),
                        PosterBackground,
                        Color(0xFF030306),
                    ),
                    radius = 1200f,
                )
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            MessagesTopBar(
                contactName = contactName,
                contactInitials = contactInitials,
                onBackClick = onBackClick,
                onProfileClick = onProfileClick,
                onMoreClick = onMoreClick,
            )

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 18.dp,
                    bottom = 18.dp,
                ),
            ) {
                items(
                    items = messages,
                    key = { it.id },
                ) { message ->
                    MessageBubble(message = message)
                    Spacer(modifier = Modifier.height(22.dp))
                }
            }

            MessageInputBar(
                value = input,
                onValueChange = { input = it },
                onSendClick = {
                    val text = input.trim()
                    if (text.isNotEmpty()) {
                        onSendClick(text)
                        input = ""
                    }
                },
            )
        }
    }
}

@Composable
private fun MessagesTopBar(
    contactName: String,
    contactInitials: String,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onMoreClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PosterTopBar)
            .statusBarsPadding()
            .height(74.dp)
            .border(width = 0.5.dp, color = PosterDivider)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.ArrowBack,
            contentDescription = "Back",
            tint = PosterTextSecondary,
            modifier = Modifier
                .size(28.dp)
                .clickable { onBackClick() },
        )

        Spacer(modifier = Modifier.width(14.dp))

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    brush = Brush.linearGradient(
                        listOf(PosterPrimary, PosterPrimaryDark),
                    ),
                    shape = CircleShape,
                )
                .clickable { onProfileClick() },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = contactInitials,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onProfileClick() },
        ) {
            Text(
                text = contactName,
                color = PosterTextPrimary,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "Online",
                color = PosterTextSecondary,
                fontSize = 14.sp,
            )
        }

        Icon(
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = "More",
            tint = PosterTextSecondary,
            modifier = Modifier
                .size(26.dp)
                .clickable { onMoreClick() },
        )
    }
}

@Composable
private fun MessageBubble(
    message: MessageUi,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isMine) Arrangement.End else Arrangement.Start,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 330.dp)
                .background(
                    color = if (message.isMine) PosterPrimary else PosterSurface,
                    shape = RoundedCornerShape(
                        topStart = 14.dp,
                        topEnd = 14.dp,
                        bottomStart = if (message.isMine) 14.dp else 5.dp,
                        bottomEnd = if (message.isMine) 5.dp else 14.dp,
                    ),
                )
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 13.dp,
                    bottom = 11.dp,
                ),
        ) {
            Text(
                text = message.text,
                color = PosterTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 21.sp,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = message.time,
                color = if (message.isMine) Color(0xFFE0DFFF) else PosterTextMuted,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
private fun MessageInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PosterTopBar)
            .border(width = 0.5.dp, color = PosterDivider)
            .navigationBarsPadding()
            .height(78.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            cursorBrush = SolidColor(PosterPrimary),
            textStyle = TextStyle(
                color = PosterTextPrimary,
                fontSize = 16.sp,
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(
                            color = PosterSurface,
                            shape = RoundedCornerShape(25.dp),
                        )
                        .border(
                            width = 1.dp,
                            color = PosterStroke.copy(alpha = 0.55f),
                            shape = RoundedCornerShape(25.dp),
                        )
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = "Type a message...",
                            color = PosterTextMuted,
                            fontSize = 16.sp,
                        )
                    }

                    innerTextField()
                }
            },
            modifier = Modifier.weight(1f),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(50.dp)
                .background(
                    brush = Brush.linearGradient(
                        listOf(PosterPrimary, PosterPrimaryDark),
                    ),
                    shape = CircleShape,
                )
                .clickable { onSendClick() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Send,
                contentDescription = "Send",
                tint = Color.White,
                modifier = Modifier.size(23.dp),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun MessagesScreenPreview() {
    MessagesScreen(
        contactName = "Alice Johnson",
        contactInitials = "AJ",
        messages = sampleMessages,
        onBackClick = {},
        onProfileClick = {},
        onMoreClick = {},
        onSendClick = {},
    )
}
