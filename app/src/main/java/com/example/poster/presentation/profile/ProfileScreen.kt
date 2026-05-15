package com.example.poster.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.poster.core.media.RemoteImageConfig
import com.example.poster.domain.model.Attachment
import com.example.poster.domain.model.AttachmentType

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

data class ProfileFileUi(
    val id: String,
    val name: String,
    val size: String,
    val date: String,
)

@Composable
fun ProfileScreen(
    initials: String = "AJ",
    name: String = "Alice Johnson",
    username: String = "@alice.johnson",
    bio: String = "Product designer passionate about creating beautiful and functional user experiences.",
    attachments: List<Attachment> = emptyList(),
    files: List<ProfileFileUi> = emptyList(),
    onBackClick: () -> Unit = {},
) {
    var selectedTab by remember { mutableStateOf(ProfileTab.MEDIA) }
    val mediaAttachments = remember(attachments) {
        attachments.filter { attachment ->
            attachment.type == AttachmentType.IMAGE || attachment.type == AttachmentType.VIDEO
        }
    }

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
            ProfileTab.MEDIA -> MediaGrid(attachments = mediaAttachments)
            ProfileTab.FILES -> FilesList(
                files = files,
                onFileClick = { file ->
                    // TODO: open/download file
                },
            )
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
private fun MediaGrid(attachments: List<Attachment>) {
    if (attachments.isEmpty()) {
        EmptyProfileTab(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Image,
                    contentDescription = null,
                    tint = PosterTextMuted,
                    modifier = Modifier.size(42.dp),
                )
            },
            text = "No media yet",
        )
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        gridItems(attachments, key = { it.id }) { attachment ->
            val context = LocalContext.current
            val imageModel = remember(context, attachment.localUri, attachment.remoteUrl) {
                attachment.localUri ?: attachment.remoteUrl?.let { remoteUrl ->
                    RemoteImageConfig.buildImageModel(
                        context = context,
                        remoteUrl = remoteUrl,
                    )
                }
            }
            val tileShape = RoundedCornerShape(7.dp)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(132.dp)
                    .clip(tileShape)
                    .background(color = PosterSurface, shape = tileShape)
                    .border(
                        width = 0.5.dp,
                        color = PosterDivider,
                        shape = tileShape,
                    ),
            ) {
                if (imageModel != null) {
                    AsyncImage(
                        model = imageModel,
                        contentDescription = attachment.fileName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Image,
                            contentDescription = null,
                            tint = PosterTextMuted,
                            modifier = Modifier.size(32.dp),
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.45f))
                        .padding(8.dp),
                ) {
                    Text(
                        text = attachment.fileName,
                        color = PosterTextPrimary,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
private fun FilesList(
    files: List<ProfileFileUi>,
    onFileClick: (ProfileFileUi) -> Unit,
) {
    if (files.isEmpty()) {
        EmptyProfileTab(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Description,
                    contentDescription = null,
                    tint = PosterTextMuted,
                    modifier = Modifier.size(42.dp),
                )
            },
            text = "No files yet",
        )
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 4.dp,
            end = 4.dp,
            top = 14.dp,
            bottom = 24.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = files,
            key = { it.id },
        ) { file ->
            ProfileFileRow(
                file = file,
                onClick = { onFileClick(file) },
            )
        }
    }
}

@Composable
private fun ProfileFileRow(
    file: ProfileFileUi,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                color = PosterSurface,
                shape = RoundedCornerShape(10.dp),
            )
            .border(
                width = 0.5.dp,
                color = PosterDivider,
                shape = RoundedCornerShape(10.dp),
            )
            .clickable { onClick() }
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            PosterPrimary,
                            PosterPrimaryDark,
                        ),
                    ),
                    shape = RoundedCornerShape(9.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Description,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp),
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = file.name,
                color = PosterTextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "${file.size} • ${file.date}",
                color = PosterTextMuted,
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
private fun EmptyProfileTab(
    icon: @Composable () -> Unit,
    text: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 72.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        icon()

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = text,
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
