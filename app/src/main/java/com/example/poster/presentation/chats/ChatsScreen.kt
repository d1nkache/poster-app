package com.example.poster.presentation.chats

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.poster.presentation.common.FeatureStubScreen

@Composable
fun ChatsScreen(modifier: Modifier = Modifier) {
    FeatureStubScreen(
        title = "Chats",
        description = "Chat list screen becomes the first top-level feature after auth.",
        checkpoints = listOf(
            "Observe chats from ChatRepository through ObserveChatsUseCase",
            "Show unread counters and last message preview",
            "Open a selected chat in the messages feature",
        ),
        modifier = modifier,
    )
}
