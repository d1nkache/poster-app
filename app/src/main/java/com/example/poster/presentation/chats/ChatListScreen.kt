package com.example.poster.presentation.chats

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.poster.domain.model.Chat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

fun Chat.toChatPreviewUi(): ChatPreviewUi {
    return ChatPreviewUi(
        id = id,
        initials = initials,
        name = title,
        lastMessage = lastMessage,
        time = lastMessageTime,
        unreadCount = unreadCount,
    )
}

@Composable
fun ChatListScreen(
    chats: List<ChatPreviewUi>,
    isSetupRequired: Boolean,
    profileAvatarUrl: String? = null,
    hasMailAccessToken: Boolean = !isSetupRequired,
    isRefreshingChats: Boolean = false,
    onChatClick: (ChatPreviewUi) -> Unit,
    onRefreshChatsClick: () -> Unit = {},
    onSetupTokenClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onProfileClick: () -> Unit = {},
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }
    var shouldFocusSearchOnShow by remember { mutableStateOf(false) }
    var isScrollingToTopForSearch by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val searchFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    fun showSearch(requestFocus: Boolean) {
        shouldFocusSearchOnShow = requestFocus
        if (!requestFocus) {
            focusManager.clearFocus()
        }
        isSearchVisible = true
    }

    fun openSearchFromIcon() {
        if (hasMailAccessToken) {
            isScrollingToTopForSearch = true
        }
        showSearch(requestFocus = true)
        if (hasMailAccessToken) {
            coroutineScope.launch {
                try {
                    listState.animateScrollToItem(0)
                } finally {
                    isScrollingToTopForSearch = false
                }
            }
        }
    }

    fun dismissSearch() {
        searchQuery = ""
        isSearchVisible = false
        shouldFocusSearchOnShow = false
        isScrollingToTopForSearch = false
        focusManager.clearFocus()
    }

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

    val isListAtTop = listState.firstVisibleItemIndex == 0 &&
        listState.firstVisibleItemScrollOffset == 0

    LaunchedEffect(isSearchVisible, shouldFocusSearchOnShow) {
        if (isSearchVisible && shouldFocusSearchOnShow) {
            delay(80)
            searchFocusRequester.requestFocus()
        }
    }

    LaunchedEffect(isSearchVisible, isListAtTop, isScrollingToTopForSearch) {
        if (isSearchVisible && !isListAtTop && !isScrollingToTopForSearch) {
            dismissSearch()
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
            )
            .pointerInput(isSearchVisible, hasMailAccessToken, isListAtTop) {
                awaitPointerEventScope {
                    while (true) {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val startY = down.position.y
                        var searchStateChangedByGesture = false

                        do {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull { it.id == down.id }
                            val dragDistance = change?.position?.y?.minus(startY) ?: 0f

                            if (
                                change != null &&
                                change.pressed &&
                                !isSearchVisible &&
                                hasMailAccessToken &&
                                isListAtTop &&
                                dragDistance > 72f
                            ) {
                                showSearch(requestFocus = false)
                                searchStateChangedByGesture = true
                            }
                        } while (event.changes.any { it.pressed } && !searchStateChangedByGesture)
                    }
                }
            },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            ChatTopBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                isSearchVisible = isSearchVisible,
                searchFocusRequester = searchFocusRequester,
                onSearchClick = ::openSearchFromIcon,
                onSearchDismiss = ::dismissSearch,
                onRefreshChatsClick = onRefreshChatsClick,
                onSettingsClick = onSettingsClick,
                onProfileClick = onProfileClick,
                profileAvatarUrl = profileAvatarUrl,
                hasMailAccessToken = hasMailAccessToken,
                isRefreshingChats = isRefreshingChats,
            )

            if (hasMailAccessToken) {
                if (filteredChats.isEmpty()) {
                    EmptyChatsContent(
                        isSearchResultEmpty = chats.isNotEmpty() && searchQuery.isNotBlank(),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .navigationBarsPadding(),
                    )
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .navigationBarsPadding(),
                        contentPadding = PaddingValues(bottom = 24.dp),
                    ) {
                        items(
                            items = filteredChats,
                            key = { it.id },
                        ) { chat ->
                            ChatPreviewItem(
                                chat = chat,
                                enabled = true,
                                onClick = { onChatClick(chat) },
                            )
                        }
                    }
                }
            } else {
                MailAccessEmptyContent(
                    isSetupRequired = isSetupRequired,
                    onSetupTokenClick = onSetupTokenClick,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )
            }
        }
    }
}

@Composable
private fun EmptyChatsContent(
    isSearchResultEmpty: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "☹",
                color = PosterTextMuted,
                fontSize = 56.sp,
                lineHeight = 56.sp,
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (isSearchResultEmpty) {
                    "Ничего не найдено"
                } else {
                    "Пока что нет диалогов"
                },
                color = PosterTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun MailAccessEmptyContent(
    isSetupRequired: Boolean,
    onSetupTokenClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        if (isSetupRequired) {
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

        MailAccessLockedNotice(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
            ),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            BlockedMailEmptyIcon(
                modifier = Modifier.padding(bottom = 42.dp),
            )
        }
    }
}

@Composable
private fun BlockedMailEmptyIcon(
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "BlockedMailEmptyIcon")
    val translationY by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "MailLevitation",
    )
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.82f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "MailGlowPulse",
    )

    Box(
        modifier = modifier
            .size(220.dp)
            .offset(y = translationY.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(128.dp * glowScale)
                .blur(30.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            PosterPrimary.copy(alpha = 0.42f),
                            Color(0xFF00D2FF).copy(alpha = 0.14f),
                            Color.Transparent,
                        ),
                    ),
                    shape = CircleShape,
                ),
        )

        Box(
            modifier = Modifier
                .size(154.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.075f),
                            PosterPrimary.copy(alpha = 0.08f),
                            Color.White.copy(alpha = 0.018f),
                        ),
                    ),
                )
                .border(
                    width = 1.dp,
                    color = PosterPrimary.copy(alpha = 0.34f),
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier.size(82.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = "Mail access token is missing",
                    tint = PosterPrimary.copy(alpha = 0.94f),
                    modifier = Modifier.fillMaxSize(),
                )
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
    isSearchVisible: Boolean,
    searchFocusRequester: FocusRequester,
    onSearchClick: () -> Unit,
    onSearchDismiss: () -> Unit,
    onRefreshChatsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onProfileClick: () -> Unit,
    profileAvatarUrl: String?,
    hasMailAccessToken: Boolean,
    isRefreshingChats: Boolean,
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
                imageUrl = profileAvatarUrl,
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

            IconButton(
                onClick = onRefreshChatsClick,
                enabled = hasMailAccessToken && !isRefreshingChats,
                modifier = Modifier.size(40.dp),
            ) {
                if (isRefreshingChats) {
                    CircularProgressIndicator(
                        color = PosterIcon,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp),
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = "Refresh chats",
                        tint = if (hasMailAccessToken) PosterTextSecondary else PosterTextMuted,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }

            IconButton(
                onClick = onSearchClick,
                modifier = Modifier
                    .size(40.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search",
                    tint = if (isSearchVisible) PosterIcon else PosterTextSecondary,
                    modifier = Modifier.size(24.dp),
                )
            }

            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier.size(40.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings",
                    tint = PosterTextSecondary,
                    modifier = Modifier.size(24.dp),
                )
            }
        }

        AnimatedVisibility(
            visible = isSearchVisible,
            enter = slideInVertically(
                animationSpec = tween(durationMillis = 220),
                initialOffsetY = { -it / 2 },
            ) + expandVertically(
                animationSpec = tween(durationMillis = 220),
                expandFrom = Alignment.Top,
            ) + fadeIn(animationSpec = tween(durationMillis = 160)),
            exit = shrinkVertically(
                animationSpec = tween(durationMillis = 180),
                shrinkTowards = Alignment.Top,
            ) + slideOutVertically(
                animationSpec = tween(durationMillis = 180),
                targetOffsetY = { -it / 2 },
            ) + fadeOut(animationSpec = tween(durationMillis = 120)),
        ) {
            SearchField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                onDismiss = onSearchDismiss,
                focusRequester = searchFocusRequester,
                modifier = Modifier.padding(top = 12.dp),
            )
        }
    }
}

@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
            .shadow(
                elevation = 18.dp,
                shape = RoundedCornerShape(16.dp),
            )
            .background(
                color = PosterSurface.copy(alpha = 0.96f),
                shape = RoundedCornerShape(16.dp),
            )
            .border(
                width = 1.dp,
                color = PosterStroke.copy(alpha = 0.75f),
                shape = RoundedCornerShape(16.dp),
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
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
        )

        IconButton(
            onClick = {
                if (value.isBlank()) {
                    onDismiss()
                } else {
                    onValueChange("")
                }
            },
            modifier = Modifier.size(32.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Close search",
                tint = PosterTextMuted,
                modifier = Modifier.size(20.dp),
            )
        }
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
    imageUrl: String? = null,
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
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
        } else if (showPersonIcon) {
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
        chats = emptyList(),
        isSetupRequired = true,
        hasMailAccessToken = false,
        isRefreshingChats = false,
        onChatClick = {},
        onRefreshChatsClick = {},
        onSetupTokenClick = {},
        onSettingsClick = {},
        onProfileClick = {},
    )
}
