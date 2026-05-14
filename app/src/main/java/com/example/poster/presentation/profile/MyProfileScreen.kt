package com.example.poster.presentation.profile

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Edit
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
import androidx.compose.ui.text.style.TextAlign
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

@Composable
fun MyProfileScreen(
    name: String = "Your Name",
    username: String = "@your.username",
    bio: String = "Hey there! I'm using Poster.",
    onBackClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onNameChange: (String) -> Unit = {},
    onUsernameChange: (String) -> Unit = {},
    onBioChange: (String) -> Unit = {},
) {
    var nameValue by remember { mutableStateOf(name) }
    var usernameValue by remember { mutableStateOf(username) }
    var bioValue by remember { mutableStateOf(bio) }

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
            )
            .navigationBarsPadding(),
    ) {
        MyProfileTopBar(
            onBackClick = onBackClick,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(195.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(138.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                PosterPrimary,
                                PosterPrimaryDark,
                            ),
                        )
                    ),
            )

            Box(
                modifier = Modifier
                    .size(116.dp)
                    .align(Alignment.BottomCenter)
                    .background(PosterBackground, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(106.dp)
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
                    Text(
                        text = getInitials(nameValue),
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }

                Box(
                    modifier = Modifier
                        .size(35.dp)
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
                        .clickable { onAvatarClick() },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CameraAlt,
                        contentDescription = "Change avatar",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = nameValue,
            color = PosterTextPrimary,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(26.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
        ) {
            Text(
                text = "Name",
                color = PosterTextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(8.dp))

            EditableProfileField(
                value = nameValue,
                onValueChange = {
                    nameValue = it
                    onNameChange(it)
                },
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Username",
                color = PosterTextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(8.dp))

            EditableProfileField(
                value = usernameValue,
                onValueChange = {
                    usernameValue = it
                    onUsernameChange(it)
                },
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Bio",
                color = PosterTextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(8.dp))

            EditableProfileField(
                value = bioValue,
                onValueChange = {
                    bioValue = it
                    onBioChange(it)
                },
            )
        }
    }
}

@Composable
private fun MyProfileTopBar(
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
            text = "My Profile",
            color = PosterTextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun EditableProfileField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .background(
                color = PosterSurface,
                shape = RoundedCornerShape(12.dp),
            )
            .border(
                width = 1.dp,
                color = PosterStroke.copy(alpha = 0.75f),
                shape = RoundedCornerShape(12.dp),
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            cursorBrush = SolidColor(PosterPrimary),
            textStyle = TextStyle(
                color = PosterTextPrimary,
                fontSize = 15.sp,
                lineHeight = 21.sp,
            ),
            modifier = Modifier.weight(1f),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = null,
            tint = PosterTextSecondary,
            modifier = Modifier.size(20.dp),
        )
    }
}

private fun getInitials(name: String): String {
    val parts = name.trim().split(" ").filter { it.isNotBlank() }

    return when {
        parts.size >= 2 -> "${parts[0].first()}${parts[1].first()}".uppercase()
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "YN"
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun MyProfileScreenPreview() {
    MyProfileScreen(
        onBackClick = {},
        onAvatarClick = {},
    )
}
