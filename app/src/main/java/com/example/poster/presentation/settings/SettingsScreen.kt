package com.example.poster.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.VpnKey
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PosterBackground = Color(0xFF030306)
private val PosterTopBar = Color(0xFF101014)
private val PosterSurface = Color(0xFF101014)
private val PosterSection = Color(0xFF090716)
private val PosterDivider = Color(0xFF1B1A25)
private val PosterPrimary = Color(0xFF625BFF)
private val PosterPrimaryDark = Color(0xFF493CCB)
private val PosterIconBg = Color(0xFF1D1A4A)
private val PosterTextPrimary = Color(0xFFF7F7FF)
private val PosterTextSecondary = Color(0xFFA5A6BA)
private val PosterTextMuted = Color(0xFF7B7D92)
private val PosterAccent = Color(0xFF7F8BFF)

@Immutable
data class SettingsItemUi(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
)

@Composable
fun SettingsScreen(
    name: String = "Your Name",
    username: String = "@your.username",
    email: String = "your.email@example.com",
    birthday: String = "January 1, 2000",
    bio: String = "Hey there! I'm using Poster.",
    language: String = "English",
    accessTokenStatus: String = "Configured",
    onBackClick: () -> Unit = {},
    onAccessTokenClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onProfilePhotoClick: () -> Unit = {},
    onAccountItemClick: (String) -> Unit = {},
) {
    val accountItems = listOf(
        SettingsItemUi(
            id = "name",
            title = "Name",
            subtitle = name,
            icon = Icons.Outlined.AccountCircle,
        ),
        SettingsItemUi(
            id = "username",
            title = "Username",
            subtitle = username,
            icon = Icons.Outlined.AlternateEmail,
        ),
        SettingsItemUi(
            id = "email",
            title = "Email",
            subtitle = email,
            icon = Icons.Outlined.Email,
        ),
        SettingsItemUi(
            id = "birthday",
            title = "Birthday",
            subtitle = birthday,
            icon = Icons.Outlined.CalendarMonth,
        ),
        SettingsItemUi(
            id = "bio",
            title = "Bio",
            subtitle = bio,
            icon = Icons.Outlined.Article,
        ),
    )

    val preferenceItems = listOf(
        SettingsItemUi(
            id = "language",
            title = "Language",
            subtitle = language,
            icon = Icons.Outlined.Language,
        ),
    )

    val securityItems = listOf(
        SettingsItemUi(
            id = "access_token",
            title = "Access Token",
            subtitle = accessTokenStatus,
            icon = Icons.Outlined.VpnKey,
        ),
        SettingsItemUi(
            id = "privacy",
            title = "Privacy & Security",
            subtitle = "",
            icon = Icons.Outlined.Security,
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF09051E),
                        PosterBackground,
                        Color(0xFF030306),
                    ),
                    radius = 1200f,
                )
            ),
    ) {
        SettingsTopBar(
            onBackClick = onBackClick,
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .navigationBarsPadding(),
            contentPadding = PaddingValues(bottom = 32.dp),
        ) {
            item {
                SettingsProfileHeader(
                    name = name,
                    username = username,
                    onProfilePhotoClick = onProfilePhotoClick,
                )
            }

            item {
                SettingsSection(
                    title = "ACCOUNT",
                    items = accountItems,
                    onItemClick = { item ->
                        onAccountItemClick(item.id)
                    },
                )
            }

            item {
                SettingsSection(
                    title = "PREFERENCES",
                    items = preferenceItems,
                    onItemClick = { item ->
                        if (item.id == "language") {
                            onLanguageClick()
                        }
                    },
                )
            }

            item {
                SettingsSection(
                    title = "SECURITY",
                    items = securityItems,
                    onItemClick = { item ->
                        when (item.id) {
                            "access_token" -> onAccessTokenClick()
                            "privacy" -> onPrivacyClick()
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun SettingsTopBar(
    onBackClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PosterTopBar)
            .statusBarsPadding()
            .height(56.dp)
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

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "Settings",
            color = PosterTextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun SettingsProfileHeader(
    name: String,
    username: String,
    onProfilePhotoClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0D0A26),
                        Color(0xFF030306),
                    ),
                )
            )
            .padding(top = 28.dp, bottom = 34.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                PosterPrimary,
                                PosterPrimaryDark,
                            ),
                        ),
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp),
                )
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.BottomEnd)
                    .shadow(
                        elevation = 12.dp,
                        shape = CircleShape,
                        spotColor = PosterPrimary.copy(alpha = 0.45f),
                    )
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                PosterPrimary,
                                PosterPrimaryDark,
                            ),
                        ),
                        shape = CircleShape,
                    )
                    .border(
                        width = 3.dp,
                        color = PosterBackground,
                        shape = CircleShape,
                    )
                    .clickable { onProfilePhotoClick() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.CameraAlt,
                    contentDescription = "Change photo",
                    tint = Color.White,
                    modifier = Modifier.size(17.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = name,
            color = PosterTextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = username,
            color = PosterAccent,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    items: List<SettingsItemUi>,
    onItemClick: (SettingsItemUi) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 26.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(PosterSection.copy(alpha = 0.75f))
                .padding(horizontal = 30.dp, vertical = 12.dp),
        ) {
            Text(
                text = title,
                color = PosterAccent,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PosterSurface.copy(alpha = 0.78f))
                .border(width = 0.5.dp, color = PosterDivider),
        ) {
            items.forEachIndexed { index, item ->
                SettingsRow(
                    item = item,
                    showDivider = index < items.lastIndex,
                    onClick = { onItemClick(item) },
                )
            }
        }
    }
}

@Composable
private fun SettingsRow(
    item: SettingsItemUi,
    showDivider: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(start = 30.dp, end = 22.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = PosterIconBg,
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = PosterAccent,
                    modifier = Modifier.size(19.dp),
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = item.title,
                    color = PosterTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                if (item.subtitle.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.subtitle,
                        color = PosterTextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = PosterTextMuted,
                modifier = Modifier.size(22.dp),
            )
        }

        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 82.dp)
                    .height(0.7.dp)
                    .background(PosterDivider),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen(
        onBackClick = {},
        onAccessTokenClick = {},
    )
}
