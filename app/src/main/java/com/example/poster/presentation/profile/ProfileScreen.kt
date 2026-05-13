package com.example.poster.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
private val PosterDivider = Color(0xFF151522)

enum class ProfileTab {
    MEDIA,
    FILES,
}

@Composable
fun ProfileScreen(
    initials: String = "AJ",
    name: String = "Alice Johnson",
    username: String = "@alice.johnson",
    bio: String = "Product designer passionate about creating beautiful and functional user experiences.",
    onBackClick: () -> Unit = {},
) {
    var selectedTab by remember { mutableStateOf(ProfileTab.MEDIA) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PosterBackground)
            .navigationBarsPadding(),
    ) {
        ProfileTopBar(
            onBackClick = onBackClick,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                PosterPrimary,
                                PosterPrimaryDark,
                            ),
                        )
                    ),
            )

            Box(
                modifier = Modifier
                    .size(126.dp)
                    .align(Alignment.BottomCenter)
                    .background(PosterBackground, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(118.dp)
                        .background(
                            brush = Brush.linearGradient(
                                listOf(PosterPrimary, PosterPrimaryDark),
                            ),
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = initials,
                        color = Color.White,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = name,
                color = PosterTextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = username,
                color = Color(0xFF7F8BFF),
                fontSize = 16.sp,
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = bio,
                color = PosterTextSecondary,
                fontSize = 16.sp,
                lineHeight = 23.sp,
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        ProfileTabs(
            selectedTab = selectedTab,
            onTabClick = { selectedTab = it },
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(PosterStroke),
        )

        when (selectedTab) {
            ProfileTab.MEDIA -> MediaGrid()
            ProfileTab.FILES -> FilesPlaceholder()
        }
    }
}

@Composable
private fun ProfileTopBar(
    onBackClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PosterTopBar)
            .statusBarsPadding()
            .height(49.dp)
            .padding(horizontal = 8.dp),
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

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Profile",
            color = PosterTextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun ProfileTabs(
    selectedTab: ProfileTab,
    onTabClick: (ProfileTab) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
    ) {
        ProfileTabItem(
            text = "Media",
            selected = selectedTab == ProfileTab.MEDIA,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Image,
                    contentDescription = null,
                    tint = if (selectedTab == ProfileTab.MEDIA) {
                        Color(0xFF7F8BFF)
                    } else {
                        PosterTextMuted
                    },
                    modifier = Modifier.size(20.dp),
                )
            },
            onClick = { onTabClick(ProfileTab.MEDIA) },
            modifier = Modifier.weight(1f),
        )

        ProfileTabItem(
            text = "Files",
            selected = selectedTab == ProfileTab.FILES,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Description,
                    contentDescription = null,
                    tint = if (selectedTab == ProfileTab.FILES) {
                        Color(0xFF7F8BFF)
                    } else {
                        PosterTextMuted
                    },
                    modifier = Modifier.size(20.dp),
                )
            },
            onClick = { onTabClick(ProfileTab.FILES) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun ProfileTabItem(
    text: String,
    selected: Boolean,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                icon()

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = text,
                    color = if (selected) Color(0xFF7F8BFF) else PosterTextMuted,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        if (selected) Color(0xFF7F8BFF) else Color.Transparent,
                    ),
            )
        }
    }
}

@Composable
private fun MediaGrid() {
    val items = remember {
        List(9) { index -> index.toString() }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(132.dp)
                    .background(
                        color = PosterSurface,
                        shape = RoundedCornerShape(7.dp),
                    )
                    .border(
                        width = 0.5.dp,
                        color = PosterDivider,
                        shape = RoundedCornerShape(7.dp),
                    ),
            )
        }
    }
}

@Composable
private fun FilesPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 72.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Outlined.Description,
            contentDescription = null,
            tint = PosterTextMuted,
            modifier = Modifier.size(42.dp),
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "No files yet",
            color = PosterTextMuted,
            fontSize = 16.sp,
        )
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen(
        initials = "AJ",
        name = "Alice Johnson",
        username = "@alice.johnson",
        bio = "Product designer passionate about creating beautiful and functional user experiences.",
        onBackClick = {},
    )
}
