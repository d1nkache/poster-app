package com.example.poster.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PosterBackground = Color(0xFF030306)
private val PosterTopBar = Color(0xFF101014)
private val PosterSurface = Color(0xFF1B1A31)
private val PosterInfoSurface = Color(0xFF0F0D27)
private val PosterStroke = Color(0xFF302B68)
private val PosterPrimary = Color(0xFF625BFF)
private val PosterPrimaryDark = Color(0xFF342983)
private val PosterTextPrimary = Color(0xFFF7F7FF)
private val PosterTextSecondary = Color(0xFFA5A6BA)
private val PosterTextMuted = Color(0xFF7B7D92)
private val PosterDivider = Color(0xFF1B1A25)

@Composable
fun AccessTokenSettingsScreen(
    initialToken: String = "",
    onBackClick: () -> Unit,
    onSaveTokenClick: (String) -> Unit,
    onHowToGetTokenClick: () -> Unit,
) {
    var token by remember { mutableStateOf(initialToken) }
    var isTokenVisible by remember { mutableStateOf(false) }

    val canSave = token.trim().isNotEmpty()

    Column(
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
        AccessTokenTopBar(
            onBackClick = onBackClick,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 23.dp),
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            AccessTokenInfoCard()

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Access Token",
                color = PosterTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(12.dp))

            AccessTokenInput(
                value = token,
                onValueChange = { token = it },
                isTokenVisible = isTokenVisible,
                onVisibilityClick = {
                    isTokenVisible = !isTokenVisible
                },
            )

            Spacer(modifier = Modifier.height(22.dp))

            HowToGetTokenButton(
                onClick = onHowToGetTokenClick,
            )

            Spacer(modifier = Modifier.height(36.dp))

            SaveTokenButton(
                enabled = canSave,
                onClick = {
                    onSaveTokenClick(token.trim())
                },
            )

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "Your token is encrypted and stored securely on your device",
                color = PosterTextMuted,
                fontSize = 13.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }
    }
}

@Composable
private fun AccessTokenTopBar(
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
            text = "Access Token Settings",
            color = PosterTextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun AccessTokenInfoCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = PosterInfoSurface,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(13.dp),
            )
            .border(
                width = 1.dp,
                color = PosterStroke,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(13.dp),
            )
            .padding(
                start = 24.dp,
                end = 22.dp,
                top = 26.dp,
                bottom = 24.dp,
            ),
    ) {
        Icon(
            imageVector = Icons.Outlined.Key,
            contentDescription = null,
            tint = PosterPrimary,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(25.dp),
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = "Why do I need an access token?",
                color = PosterTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Poster uses SMTP/IMAP protocols to send and receive messages through your email provider. An access token allows the app to securely connect to your email without storing your password.",
                color = PosterTextSecondary,
                fontSize = 15.sp,
                lineHeight = 23.sp,
            )
        }
    }
}

@Composable
private fun AccessTokenInput(
    value: String,
    onValueChange: (String) -> Unit,
    isTokenVisible: Boolean,
    onVisibilityClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(
                color = PosterSurface,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(13.dp),
            )
            .border(
                width = 1.dp,
                color = PosterStroke.copy(alpha = 0.7f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(13.dp),
            )
            .padding(start = 16.dp, end = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            cursorBrush = SolidColor(PosterPrimary),
            textStyle = TextStyle(
                color = PosterTextPrimary,
                fontSize = 14.sp,
                letterSpacing = 1.sp,
            ),
            keyboardOptions = KeyboardOptions.Default,
            visualTransformation = if (isTokenVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = "Paste your access token here",
                            color = PosterTextMuted,
                            fontSize = 14.sp,
                            letterSpacing = 1.sp,
                        )
                    }

                    innerTextField()
                }
            },
            modifier = Modifier.weight(1f),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Icon(
            imageVector = if (isTokenVisible) {
                Icons.Outlined.VisibilityOff
            } else {
                Icons.Outlined.Visibility
            },
            contentDescription = "Toggle token visibility",
            tint = PosterTextSecondary,
            modifier = Modifier
                .size(22.dp)
                .clickable { onVisibilityClick() },
        )
    }
}

@Composable
private fun HowToGetTokenButton(
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.HelpOutline,
            contentDescription = null,
            tint = Color(0xFF7F8BFF),
            modifier = Modifier.size(21.dp),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "How to get an access token?",
            color = Color(0xFF7F8BFF),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun SaveTokenButton(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = if (enabled) 24.dp else 0.dp,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(13.dp),
                spotColor = PosterPrimary.copy(alpha = 0.35f),
            )
            .background(
                brush = if (enabled) {
                    Brush.horizontalGradient(
                        colors = listOf(
                            PosterPrimary,
                            PosterPrimaryDark,
                        ),
                    )
                } else {
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF312A7A),
                            Color(0xFF261D65),
                        ),
                    )
                },
                shape = androidx.compose.foundation.shape.RoundedCornerShape(13.dp),
            )
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Save Token",
            color = if (enabled) Color.White else PosterTextMuted,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun AccessTokenSettingsScreenPreview() {
    AccessTokenSettingsScreen(
        onBackClick = {},
        onSaveTokenClick = {},
        onHowToGetTokenClick = {},
    )
}
