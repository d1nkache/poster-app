package com.example.poster.presentation.settings

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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PosterBackground = Color(0xFF030306)
private val PosterTopBar = Color(0xFF101014)
private val PosterSurface = Color(0xFF1B1A31)
private val PosterInfoSurface = Color(0xFF0F0D27)
private val PosterStroke = Color(0xFF302B68)
private val PosterPrimary = Color(0xFF625BFF)
private val PosterPrimaryDark = Color(0xFF493CCB)
private val PosterTextPrimary = Color(0xFFF7F7FF)
private val PosterTextSecondary = Color(0xFFA5A6BA)
private val PosterTextMuted = Color(0xFF7B7D92)
private val PosterDivider = Color(0xFF1B1A25)
private val PosterAccent = Color(0xFF7F8BFF)

@Immutable
data class PrivacySettingsUi(
    val showOnlineStatus: Boolean,
    val sendReadReceipts: Boolean,
    val confirmBeforeOpeningFiles: Boolean,
)

@Composable
fun SettingsTextEditScreen(
    title: String,
    label: String,
    value: String,
    placeholder: String,
    onBackClick: () -> Unit,
    onSaveClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    allowEmpty: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    var editedValue by remember(value) { mutableStateOf(value) }
    val normalizedValue = editedValue.trim()
    val canSave = allowEmpty || normalizedValue.isNotEmpty()

    SettingsDetailScaffold(
        title = title,
        onBackClick = onBackClick,
        modifier = modifier,
    ) {
        item {
            Text(
                text = label,
                color = PosterTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 23.dp),
            )
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            SettingsEditField(
                value = editedValue,
                onValueChange = { editedValue = it },
                placeholder = placeholder,
                singleLine = singleLine,
                keyboardType = keyboardType,
                modifier = Modifier.padding(horizontal = 23.dp),
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            SettingsPrimaryButton(
                text = "Save",
                enabled = canSave,
                onClick = { onSaveClick(if (allowEmpty) editedValue.trim() else normalizedValue) },
                modifier = Modifier.padding(horizontal = 23.dp),
            )
        }
    }
}

@Composable
fun LanguageSettingsScreen(
    currentLanguage: String,
    onBackClick: () -> Unit,
    onSaveLanguageClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedLanguage by rememberSaveable(currentLanguage) {
        mutableStateOf(currentLanguage)
    }
    val languages = listOf(
        SettingsOption(
            value = "English",
            title = "English",
            subtitle = "Interface labels and placeholders stay in English",
        ),
        SettingsOption(
            value = "Русский",
            title = "Русский",
            subtitle = "Основной язык интерфейса: русский",
        ),
        SettingsOption(
            value = "Deutsch",
            title = "Deutsch",
            subtitle = "UI-Sprache auf Deutsch umschalten",
        ),
    )

    SettingsDetailScaffold(
        title = "Language",
        onBackClick = onBackClick,
        modifier = modifier,
    ) {
        items(languages.size) { index ->
            val option = languages[index]
            SettingsSelectableRow(
                title = option.title,
                subtitle = option.subtitle,
                selected = selectedLanguage == option.value,
                onClick = { selectedLanguage = option.value },
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            SettingsPrimaryButton(
                text = "Save Language",
                enabled = true,
                onClick = { onSaveLanguageClick(selectedLanguage) },
                modifier = Modifier.padding(horizontal = 23.dp),
            )
        }
    }
}

@Composable
fun PrivacySettingsScreen(
    settings: PrivacySettingsUi,
    onBackClick: () -> Unit,
    onSavePrivacyClick: (PrivacySettingsUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showOnlineStatus by rememberSaveable(settings.showOnlineStatus) {
        mutableStateOf(settings.showOnlineStatus)
    }
    var sendReadReceipts by rememberSaveable(settings.sendReadReceipts) {
        mutableStateOf(settings.sendReadReceipts)
    }
    var confirmBeforeOpeningFiles by rememberSaveable(settings.confirmBeforeOpeningFiles) {
        mutableStateOf(settings.confirmBeforeOpeningFiles)
    }

    SettingsDetailScaffold(
        title = "Privacy & Security",
        onBackClick = onBackClick,
        modifier = modifier,
    ) {
        item {
            SettingsInfoCard(
                title = "Security controls",
                description = "These preferences are applied immediately in this session and stay active while the app process is alive.",
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            SettingsSwitchRow(
                title = "Show online status",
                subtitle = "Contacts can see that you are online",
                checked = showOnlineStatus,
                onCheckedChange = { showOnlineStatus = it },
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        item {
            SettingsSwitchRow(
                title = "Read receipts",
                subtitle = "Allow contacts to see when messages are read",
                checked = sendReadReceipts,
                onCheckedChange = { sendReadReceipts = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            )
        }

        item {
            SettingsSwitchRow(
                title = "Confirm files",
                subtitle = "Ask before opening downloaded attachments",
                checked = confirmBeforeOpeningFiles,
                onCheckedChange = { confirmBeforeOpeningFiles = it },
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            SettingsPrimaryButton(
                text = "Save Privacy Settings",
                enabled = true,
                onClick = {
                    onSavePrivacyClick(
                        PrivacySettingsUi(
                            showOnlineStatus = showOnlineStatus,
                            sendReadReceipts = sendReadReceipts,
                            confirmBeforeOpeningFiles = confirmBeforeOpeningFiles,
                        )
                    )
                },
                modifier = Modifier.padding(horizontal = 23.dp),
            )
        }
    }
}

@Composable
fun AccessTokenGuideScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsDetailScaffold(
        title = "How to get token",
        onBackClick = onBackClick,
        modifier = modifier,
    ) {
        item {
            SettingsInfoCard(
                title = "Use an app password",
                description = "For most mail providers this token is an app-specific password. Create it in your email account security settings and paste it into Poster.",
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        items(tokenSteps.size) { index ->
            TokenStepRow(
                index = index + 1,
                text = tokenSteps[index],
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Composable
private fun SettingsDetailScaffold(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit,
) {
    Column(
        modifier = modifier
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
        SettingsDetailTopBar(
            title = title,
            onBackClick = onBackClick,
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            contentPadding = PaddingValues(top = 24.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.Top,
            content = content,
        )
    }
}

@Composable
private fun SettingsDetailTopBar(
    title: String,
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
            text = title,
            color = PosterTextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun SettingsEditField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean,
    keyboardType: KeyboardType,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = if (singleLine) 56.dp else 148.dp)
            .background(
                color = PosterSurface,
                shape = RoundedCornerShape(13.dp),
            )
            .border(
                width = 1.dp,
                color = PosterStroke.copy(alpha = 0.75f),
                shape = RoundedCornerShape(13.dp),
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        contentAlignment = Alignment.TopStart,
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            cursorBrush = SolidColor(PosterPrimary),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            textStyle = TextStyle(
                color = PosterTextPrimary,
                fontSize = 15.sp,
                lineHeight = 22.sp,
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopStart,
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = PosterTextMuted,
                            fontSize = 15.sp,
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
private fun SettingsSelectableRow(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .background(
                color = if (selected) PosterPrimary.copy(alpha = 0.2f) else PosterSurface.copy(alpha = 0.82f),
                shape = RoundedCornerShape(15.dp),
            )
            .border(
                width = 1.dp,
                color = if (selected) PosterAccent else PosterStroke.copy(alpha = 0.7f),
                shape = RoundedCornerShape(15.dp),
            )
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = PosterTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = subtitle,
                color = PosterTextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp,
            )
        }

        Icon(
            imageVector = if (selected) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (selected) PosterAccent else PosterTextMuted,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = PosterSurface.copy(alpha = 0.82f),
                shape = RoundedCornerShape(15.dp),
            )
            .border(
                width = 1.dp,
                color = PosterStroke.copy(alpha = 0.7f),
                shape = RoundedCornerShape(15.dp),
            )
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = PosterTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = subtitle,
                color = PosterTextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp,
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
private fun SettingsInfoCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = PosterInfoSurface,
                shape = RoundedCornerShape(15.dp),
            )
            .border(
                width = 1.dp,
                color = PosterStroke.copy(alpha = 0.75f),
                shape = RoundedCornerShape(15.dp),
            )
            .padding(horizontal = 18.dp, vertical = 18.dp),
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(PosterPrimary.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = PosterAccent,
                modifier = Modifier.size(20.dp),
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = PosterTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                color = PosterTextSecondary,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            )
        }
    }
}

@Composable
private fun TokenStepRow(
    index: Int,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .background(
                color = PosterSurface.copy(alpha = 0.82f),
                shape = RoundedCornerShape(15.dp),
            )
            .border(
                width = 1.dp,
                color = PosterStroke.copy(alpha = 0.7f),
                shape = RoundedCornerShape(15.dp),
            )
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(PosterPrimary, PosterPrimaryDark),
                    ),
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = index.toString(),
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            color = PosterTextSecondary,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun SettingsPrimaryButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = if (enabled) 24.dp else 0.dp,
                shape = RoundedCornerShape(13.dp),
                spotColor = PosterPrimary.copy(alpha = 0.35f),
            )
            .background(
                brush = Brush.horizontalGradient(
                    colors = if (enabled) {
                        listOf(PosterPrimary, PosterPrimaryDark)
                    } else {
                        listOf(Color(0xFF312A7A), Color(0xFF261D65))
                    },
                ),
                shape = RoundedCornerShape(13.dp),
            )
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = if (enabled) Color.White else PosterTextMuted,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

private data class SettingsOption(
    val value: String,
    val title: String,
    val subtitle: String,
)

private val tokenSteps = listOf(
    "Open your email provider account settings.",
    "Find Security, App passwords, SMTP/IMAP access, or External apps.",
    "Create a new app password for Poster and copy the generated token.",
    "Return to Poster, paste the token, then press Save Token.",
)

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun SettingsTextEditScreenPreview() {
    SettingsTextEditScreen(
        title = "Name",
        label = "Name",
        value = "Your Name",
        placeholder = "Enter your name",
        onBackClick = {},
        onSaveClick = {},
    )
}
