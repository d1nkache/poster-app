package com.example.poster.presentation.chats

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
private val PosterDivider = Color(0xFF11111A)
private val PosterIcon = Color(0xFF8B93FF)

data class ChatPreviewUi(
    val id: String,
    val initials: String,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int = 0,
)

internal val sampleChatPreviews = listOf(
    ChatPreviewUi(
        id = "1",
        initials = "AJ",
        name = "Alice Johnson",
        lastMessage = "See you tomorrow!",
        time = "10:30 AM",
        unreadCount = 2,
    ),
    ChatPreviewUi(
        id = "2",
        initials = "BS",
        name = "Bob Smith",
        lastMessage = "Thanks for the update",
        time = "Yesterday",
    ),
    ChatPreviewUi(
        id = "3",
        initials = "CW",
        name = "Carol White",
        lastMessage = "Meeting at 3 PM?",
        time = "Yesterday",
        unreadCount = 1,
    ),
    ChatPreviewUi(
        id = "4",
        initials = "DB",
        name = "David Brown",
        lastMessage = "Perfect, sounds good!",
        time = "2 days ago",
    ),
    ChatPreviewUi(
        id = "5",
        initials = "ED",
        name = "Emma Davis",
        lastMessage = "I'll send the files",
        time = "3 days ago",
    ),
    ChatPreviewUi(
        id = "6",
        initials = "FK",
        name = "Frank King",
        lastMessage = "SMTP sync completed",
        time = "Monday",
    ),
    ChatPreviewUi(
        id = "7",
        initials = "GL",
        name = "Grace Lee",
        lastMessage = "Can we review the IMAP logs?",
        time = "Sunday",
        unreadCount = 4,
    ),
    ChatPreviewUi(
        id = "8",
        initials = "HM",
        name = "Henry Miller",
        lastMessage = "Token settings look correct",
        time = "Saturday",
    ),
    ChatPreviewUi(
        id = "9",
        initials = "IN",
        name = "Ivy Nelson",
        lastMessage = "New message arrived via mail",
        time = "Friday",
        unreadCount = 3,
    ),
    ChatPreviewUi(
        id = "10",
        initials = "JO",
        name = "Jack Owens",
        lastMessage = "I'll check the server response",
        time = "Friday",
    ),
    ChatPreviewUi(
        id = "11",
        initials = "KP",
        name = "Kate Parker",
        lastMessage = "Thanks, it works now",
        time = "Thursday",
    ),
    ChatPreviewUi(
        id = "12",
        initials = "LR",
        name = "Liam Reed",
        lastMessage = "Let's deploy the Ktor endpoint",
        time = "Wednesday",
        unreadCount = 1,
    ),
)

@Composable
fun ChatListScreen(
    chats: List<ChatPreviewUi>,
    isSetupRequired: Boolean,
    hasMailAccessToken: Boolean = !isSetupRequired,
    onChatClick: (ChatPreviewUi) -> Unit,
    onSetupTokenClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onProfileClick: () -> Unit = {},
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredChats = remember(searchQuery, chats) {
        if (searchQuery.isBlank()) {
            chats
        } else {
            chats.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                    it.lastMessage.contains(searchQuery, ignoreCase = true)
            }
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
                    radius = 1100f,
                )
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            ChatTopBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onSettingsClick = onSettingsClick,
                onProfileClick = onProfileClick,
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                contentPadding = PaddingValues(bottom = 24.dp),
            ) {
                if (isSetupRequired) {
                    item {
                        SetupTokenAlert(
                            onSetupTokenClick = onSetupTokenClick,
                            modifier = Modifier.padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 16.dp,
                                bottom = 12.dp,
                            ),
                        )
                    }

                    item {
                        MailAccessLockedNotice(
                            modifier = Modifier.padding(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 10.dp,
                            ),
                        )
                    }
                }

                items(
                    items = filteredChats,
                    key = { it.id },
                ) { chat ->
                    ChatPreviewItem(
                        chat = chat,
                        enabled = hasMailAccessToken,
                        onClick = { onChatClick(chat) },
                    )
                }
            }
        }
    }
}

@Composable
private fun MailAccessLockedNotice(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF100F1D),
                shape = RoundedCornerShape(14.dp),
            )
            .border(
                width = 1.dp,
                color = PosterStroke.copy(alpha = 0.75f),
                shape = RoundedCornerShape(14.dp),
            )
            .padding(horizontal = 18.dp, vertical = 16.dp),
    ) {
        Text(
            text = "Чаты пока грустят",
            color = PosterTextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(7.dp))

        Text(
            text = "Без mail access token нельзя читать чаты и отправлять сообщения. Настрой токен, и Poster снова оживет.",
            color = PosterTextSecondary,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        )
    }
}

@Composable
private fun ChatTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSettingsClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PosterTopBar)
            .statusBarsPadding()
            .padding(start = 16.dp, end = 14.dp, top = 16.dp, bottom = 12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AvatarCircle(
                text = "",
                size = 40.dp,
                showPersonIcon = true,
                modifier = Modifier.clickable { onProfileClick() },
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Poster",
                color = PosterTextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )

            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "Settings",
                tint = PosterTextSecondary,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onSettingsClick() },
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        SearchField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
        )
    }
}

@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .background(
                color = PosterSurface,
                shape = RoundedCornerShape(9.dp),
            )
            .border(
                width = 1.dp,
                color = PosterStroke.copy(alpha = 0.75f),
                shape = RoundedCornerShape(9.dp),
            )
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = null,
            tint = PosterTextMuted,
            modifier = Modifier.size(22.dp),
        )

        Spacer(modifier = Modifier.width(10.dp))

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
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = "Search messages",
                            color = PosterTextMuted,
                            fontSize = 16.sp,
                        )
                    }

                    innerTextField()
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun SetupTokenAlert(
    onSetupTokenClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(74.dp)
            .fillMaxWidth()
            .background(
                color = Color(0xFF15142D),
                shape = RoundedCornerShape(14.dp),
            )
            .border(
                width = 1.dp,
                color = PosterPrimary.copy(alpha = 0.65f),
                shape = RoundedCornerShape(14.dp),
            )
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            tint = PosterIcon,
            modifier = Modifier.size(22.dp),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "Setup required to receive messages",
            color = PosterTextPrimary,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .height(44.dp)
                .shadow(
                    elevation = 14.dp,
                    shape = RoundedCornerShape(10.dp),
                )
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(PosterPrimary, PosterPrimaryDark),
                    ),
                    shape = RoundedCornerShape(10.dp),
                )
                .clickable { onSetupTokenClick() }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Setup Token",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun ChatPreviewItem(
    chat: ChatPreviewUi,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AvatarCircle(
            text = chat.initials,
            size = 48.dp,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
        ) {
            Text(
                text = chat.name,
                color = if (enabled) PosterTextPrimary else PosterTextMuted,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (enabled) {
                    chat.lastMessage
                } else {
                    "Недоступно без mail access token"
                },
                color = if (enabled) PosterTextSecondary else PosterTextMuted,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = if (enabled) chat.time else "--",
                color = PosterTextMuted,
                fontSize = 13.sp,
            )

            if (enabled && chat.unreadCount > 0) {
                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            color = PosterPrimary,
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = chat.unreadCount.toString(),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(PosterDivider.copy(alpha = 0.75f)),
    )
}

@Composable
private fun AvatarCircle(
    text: String,
    size: Dp,
    showPersonIcon: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(size)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        PosterPrimary,
                        Color(0xFF3D39B8),
                    ),
                ),
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (showPersonIcon) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp),
            )
        } else {
            Text(
                text = text,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun ChatListScreenPreview() {
    ChatListScreen(
        chats = sampleChatPreviews,
        isSetupRequired = true,
        hasMailAccessToken = false,
        onChatClick = {},
        onSetupTokenClick = {},
        onSettingsClick = {},
        onProfileClick = {},
    )
}
