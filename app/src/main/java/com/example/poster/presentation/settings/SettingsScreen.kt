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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.VpnKey
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
    onAccountValueSave: (String, String) -> Unit = { _, _ -> },
) {
    var currentName by rememberSaveable(name) { mutableStateOf(name) }
    var currentUsername by rememberSaveable(username) { mutableStateOf(username) }
    var currentEmail by rememberSaveable(email) { mutableStateOf(email) }
    var currentBirthday by rememberSaveable(birthday) { mutableStateOf(birthday) }
    var currentBio by rememberSaveable(bio) { mutableStateOf(bio) }
    var editingAccountItemId by rememberSaveable { mutableStateOf<String?>(null) }
    var editingAccountValue by rememberSaveable { mutableStateOf("") }

    fun accountValueFor(id: String): String {
        return when (id) {
            "name" -> currentName
            "username" -> currentUsername
            "email" -> currentEmail
            "birthday" -> currentBirthday
            "bio" -> currentBio
            else -> ""
        }
    }

    fun saveAccountValue(id: String, value: String) {
        val normalizedValue = normalizeAccountValue(id, value)
        when (id) {
            "name" -> currentName = normalizedValue
            "username" -> currentUsername = normalizedValue
            "email" -> currentEmail = normalizedValue
            "birthday" -> currentBirthday = normalizedValue
            "bio" -> currentBio = normalizedValue
        }
        editingAccountItemId = null
        editingAccountValue = ""
        onAccountValueSave(id, normalizedValue)
    }

    val accountItems = remember(
        currentName,
        currentUsername,
        currentEmail,
        currentBirthday,
        currentBio,
    ) {
        listOf(
            SettingsItemUi(
                id = "name",
                title = "Name",
                subtitle = currentName,
                icon = Icons.Outlined.AccountCircle,
            ),
            SettingsItemUi(
                id = "username",
                title = "Username",
                subtitle = currentUsername,
                icon = Icons.Outlined.AlternateEmail,
            ),
            SettingsItemUi(
                id = "email",
                title = "Email",
                subtitle = currentEmail,
                icon = Icons.Outlined.Email,
            ),
            SettingsItemUi(
                id = "birthday",
                title = "Birthday",
                subtitle = currentBirthday,
                icon = Icons.Outlined.CalendarMonth,
            ),
            SettingsItemUi(
                id = "bio",
                title = "Bio",
                subtitle = currentBio,
                icon = Icons.Outlined.Article,
            ),
        )
    }

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
                    name = currentName,
                    username = currentUsername,
                    onProfilePhotoClick = onProfilePhotoClick,
                )
            }

            item {
                SettingsSection(
                    title = "ACCOUNT",
                    items = accountItems,
                    editingItemId = editingAccountItemId,
                    editingValue = editingAccountValue,
                    onEditingValueChange = { editingAccountValue = it },
                    onEditClick = { item ->
                        editingAccountItemId = item.id
                        editingAccountValue = accountValueFor(item.id)
                    },
                    onSaveEdit = { item ->
                        if (canSaveAccountValue(item.id, editingAccountValue)) {
                            saveAccountValue(item.id, editingAccountValue)
                        }
                    },
                    onCancelEdit = {
                        editingAccountItemId = null
                        editingAccountValue = ""
                    },
                    onItemClick = { item ->
                        editingAccountItemId = item.id
                        editingAccountValue = accountValueFor(item.id)
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
    editingItemId: String? = null,
    editingValue: String = "",
    onEditingValueChange: (String) -> Unit = {},
    onEditClick: (SettingsItemUi) -> Unit = onItemClick,
    onSaveEdit: (SettingsItemUi) -> Unit = {},
    onCancelEdit: () -> Unit = {},
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
                val isEditing = item.id == editingItemId
                SettingsRow(
                    item = item,
                    showDivider = index < items.lastIndex,
                    isEditing = isEditing,
                    editingValue = if (isEditing) editingValue else "",
                    onEditingValueChange = onEditingValueChange,
                    onSaveEdit = { onSaveEdit(item) },
                    onCancelEdit = onCancelEdit,
                    onClick = {
                        if (isEditing) {
                            onCancelEdit()
                        } else {
                            onEditClick(item)
                        }
                    },
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
    isEditing: Boolean = false,
    editingValue: String = "",
    onEditingValueChange: (String) -> Unit = {},
    onSaveEdit: () -> Unit = {},
    onCancelEdit: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .clickable { onClick() }
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

            if (isEditing) {
                IconButton(
                    onClick = onCancelEdit,
                    modifier = Modifier.size(34.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Cancel",
                        tint = PosterTextMuted,
                        modifier = Modifier.size(19.dp),
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = PosterTextMuted,
                    modifier = Modifier.size(22.dp),
                )
            }
        }

        if (isEditing) {
            InlineSettingsEditor(
                itemId = item.id,
                value = editingValue,
                onValueChange = onEditingValueChange,
                onSaveClick = onSaveEdit,
                canSave = canSaveAccountValue(item.id, editingValue),
                modifier = Modifier.padding(
                    start = 82.dp,
                    end = 22.dp,
                    bottom = 14.dp,
                ),
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

@Composable
private fun InlineSettingsEditor(
    itemId: String,
    value: String,
    onValueChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    canSave: Boolean,
    modifier: Modifier = Modifier,
) {
    val singleLine = itemId != "bio"
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(if (singleLine) 46.dp else 92.dp)
                .background(
                    color = PosterSection.copy(alpha = 0.86f),
                    shape = RoundedCornerShape(12.dp),
                )
                .border(
                    width = 1.dp,
                    color = PosterPrimary.copy(alpha = 0.45f),
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(horizontal = 14.dp, vertical = 11.dp),
            contentAlignment = Alignment.TopStart,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = singleLine,
                cursorBrush = SolidColor(PosterPrimary),
                keyboardOptions = KeyboardOptions(
                    keyboardType = accountKeyboardType(itemId),
                ),
                textStyle = TextStyle(
                    color = PosterTextPrimary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.TopStart,
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = accountPlaceholder(itemId),
                                color = PosterTextMuted,
                                fontSize = 14.sp,
                            )
                        }

                        innerTextField()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        IconButton(
            onClick = onSaveClick,
            enabled = canSave,
            modifier = Modifier
                .size(42.dp)
                .background(
                    color = if (canSave) PosterPrimary.copy(alpha = 0.2f) else PosterDivider,
                    shape = CircleShape,
                ),
        ) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = "Save",
                tint = if (canSave) PosterAccent else PosterTextMuted,
                modifier = Modifier.size(21.dp),
            )
        }
    }
}

private fun accountKeyboardType(itemId: String): KeyboardType {
    return when (itemId) {
        "email" -> KeyboardType.Email
        else -> KeyboardType.Text
    }
}

private fun accountPlaceholder(itemId: String): String {
    return when (itemId) {
        "name" -> "Enter name"
        "username" -> "@username"
        "email" -> "email@example.com"
        "birthday" -> "Birthday"
        "bio" -> "Write a short bio"
        else -> ""
    }
}

private fun canSaveAccountValue(itemId: String, value: String): Boolean {
    return itemId == "bio" || value.trim().isNotEmpty()
}

private fun normalizeAccountValue(itemId: String, value: String): String {
    val trimmedValue = value.trim()
    return when {
        itemId == "bio" -> trimmedValue
        itemId == "username" && trimmedValue.isNotEmpty() && !trimmedValue.startsWith("@") -> "@$trimmedValue"
        else -> trimmedValue
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
