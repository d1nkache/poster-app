package com.example.poster.presentation.chats

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize

@Composable
fun ChatsScreen(
    chats: List<ChatPreviewUi> = emptyList(),
    isSetupRequired: Boolean = true,
    hasMailAccessToken: Boolean = !isSetupRequired,
    profileAvatarUrl: String? = null,
    onChatClick: (ChatPreviewUi) -> Unit = {},
    onSetupTokenClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        ChatListScreen(
            chats = chats,
            isSetupRequired = isSetupRequired,
            hasMailAccessToken = hasMailAccessToken,
            profileAvatarUrl = profileAvatarUrl,
            onChatClick = onChatClick,
            onSetupTokenClick = onSetupTokenClick,
            onSettingsClick = onSettingsClick,
            onProfileClick = onProfileClick,
        )
    }
}
