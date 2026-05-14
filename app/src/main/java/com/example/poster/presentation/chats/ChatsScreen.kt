package com.example.poster.presentation.chats

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize

@Composable
fun ChatsScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        ChatListScreen(
            chats = sampleChatPreviews,
            isSetupRequired = true,
            onChatClick = {},
            onSetupTokenClick = {},
            onSettingsClick = {},
            onProfileClick = {},
        )
    }
}
