package com.example.poster.presentation.messages

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.poster.core.media.RemoteImageConfig
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
private val PosterDivider = Color(0xFF1B1A25)
private val PosterIcon = Color(0xFF7F8BFF)

@Composable
fun MessagesScreen(
    contactName: String = "Alice Johnson",
    contactInitials: String = "AJ",
    messages: List<MessageUi> = emptyList(),
    canSendMessages: Boolean = true,
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
    onSendClick: (String) -> Unit = {},
    onFilePicked: (Uri) -> Unit = {},
    onImagePicked: (Uri) -> Unit = {},
) {
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val lastMessageId = messages.lastOrNull()?.id

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        uri ?: return@rememberLauncherForActivityResult

        onFilePicked(uri)
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        uri ?: return@rememberLauncherForActivityResult

        onImagePicked(uri)
    }

    LaunchedEffect(lastMessageId) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
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
                    radius = 1200f,
                )
            ),
    ) {
        ChatWallpaper(modifier = Modifier.fillMaxSize())

        Column(modifier = Modifier.fillMaxSize()) {
            MessagesTopBar(
                contactName = contactName,
                contactInitials = contactInitials,
                onBackClick = onBackClick,
                onProfileClick = onProfileClick,
                onMoreClick = onMoreClick,
            )

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 18.dp,
                    bottom = 18.dp,
                ),
            ) {
                items(
                    items = messages,
                    key = { it.id },
                ) { message ->
                    MessageBubble(message = message)
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }

            MessageInputBar(
                value = input,
                onValueChange = { input = it },
                enabled = canSendMessages,
                onAttachFileClick = {
                    if (canSendMessages) {
                        filePickerLauncher.launch(
                            arrayOf(
                                "application/pdf",
                                "application/msword",
                                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                                "text/plain",
                                "application/zip",
                            ),
                        )
                    }
                },
                onAttachImageClick = {
                    if (canSendMessages) {
                        imagePickerLauncher.launch(arrayOf("image/*"))
                    }
                },
                onSendClick = {
                    val text = input.trim()
                    if (canSendMessages && text.isNotEmpty()) {
                        onSendClick(text)
                        input = ""
                    }
                },
            )
        }
    }
}

@Composable
private fun ChatWallpaper(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val dotColor = Color(0xFF403A92).copy(alpha = 0.16f)
        val ringColor = Color(0xFF625BFF).copy(alpha = 0.08f)

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF625BFF).copy(alpha = 0.22f),
                    Color.Transparent,
                ),
                center = androidx.compose.ui.geometry.Offset(width * 0.22f, height * 0.16f),
                radius = width * 0.7f,
            ),
            radius = width * 0.7f,
            center = androidx.compose.ui.geometry.Offset(width * 0.22f, height * 0.16f),
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF21146A).copy(alpha = 0.32f),
                    Color.Transparent,
                ),
                center = androidx.compose.ui.geometry.Offset(width * 0.92f, height * 0.78f),
                radius = width * 0.82f,
            ),
            radius = width * 0.82f,
            center = androidx.compose.ui.geometry.Offset(width * 0.92f, height * 0.78f),
        )

        val spacing = 42.dp.toPx()
        var y = spacing
        var row = 0
        while (y < height) {
            var x = if (row % 2 == 0) spacing * 0.5f else spacing
            while (x < width) {
                drawCircle(
                    color = dotColor,
                    radius = 1.35.dp.toPx(),
                    center = androidx.compose.ui.geometry.Offset(x, y),
                )
                x += spacing
            }
            row += 1
            y += spacing
        }

        listOf(
            androidx.compose.ui.geometry.Offset(width * 0.78f, height * 0.22f) to 44.dp.toPx(),
            androidx.compose.ui.geometry.Offset(width * 0.18f, height * 0.72f) to 58.dp.toPx(),
            androidx.compose.ui.geometry.Offset(width * 0.68f, height * 0.58f) to 32.dp.toPx(),
        ).forEach { (center, radius) ->
            drawCircle(
                color = ringColor,
                radius = radius,
                center = center,
                style = Stroke(width = 1.1.dp.toPx()),
            )
        }
    }
}

@Composable
private fun MessagesTopBar(
    contactName: String,
    contactInitials: String,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onMoreClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PosterTopBar)
            .statusBarsPadding()
            .padding(start = 12.dp, end = 10.dp, top = 8.dp, bottom = 10.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 52.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = "Back",
                tint = PosterTextSecondary,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBackClick() },
            )

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(PosterPrimary, PosterPrimaryDark),
                        ),
                        shape = CircleShape,
                    )
                    .clickable { onProfileClick() },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = contactInitials,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onProfileClick() },
            ) {
                Text(
                    text = contactName,
                    color = PosterTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Online",
                    color = PosterTextSecondary,
                    fontSize = 12.sp,
                )
            }

            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "More",
                tint = PosterTextSecondary,
                modifier = Modifier
                    .size(22.dp)
                    .clickable { onMoreClick() },
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(1.dp)
                .background(PosterDivider),
        )
    }
}

@Composable
private fun MessageBubble(
    message: MessageUi,
    modifier: Modifier = Modifier,
) {
    val alpha = remember(message.id) { Animatable(0f) }
    val scale = remember(message.id) { Animatable(0.94f) }

    LaunchedEffect(message.id) {
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 180),
            )
        }
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow,
                ),
            )
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                this.alpha = alpha.value
                scaleX = scale.value
                scaleY = scale.value
                transformOrigin = TransformOrigin(
                    pivotFractionX = if (message.isMine) 1f else 0f,
                    pivotFractionY = 1f,
                )
            },
        horizontalArrangement = if (message.isMine) Arrangement.End else Arrangement.Start,
    ) {
        Box(
            modifier = Modifier.widthIn(
                max = if (message.type == MessageContentType.IMAGE) 190.dp else 420.dp,
            ),
        ) {
            when (message.type) {
                MessageContentType.TEXT -> TextMessageBubble(message = message)
                MessageContentType.FILE -> FileMessageBubble(message = message)
                MessageContentType.IMAGE -> ImageMessageBubble(message = message)
            }
        }
    }
}

@Composable
private fun TextMessageBubble(
    message: MessageUi,
) {
    Column(
        modifier = Modifier
            .background(
                color = if (message.isMine) PosterPrimary else PosterSurface,
                shape = RoundedCornerShape(
                    topStart = 14.dp,
                    topEnd = 14.dp,
                    bottomStart = if (message.isMine) 14.dp else 5.dp,
                    bottomEnd = if (message.isMine) 5.dp else 14.dp,
                ),
            )
            .padding(start = 16.dp, end = 16.dp, top = 13.dp, bottom = 11.dp),
    ) {
        Text(
            text = message.text,
            color = PosterTextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 21.sp,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = message.time,
            color = if (message.isMine) Color(0xFFE0DFFF) else PosterTextMuted,
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun FileMessageBubble(
    message: MessageUi,
) {
    val attachment = message.attachment ?: return

    Column(
        modifier = Modifier
            .background(
                color = if (message.isMine) PosterPrimary else PosterSurface,
                shape = RoundedCornerShape(
                    topStart = 14.dp,
                    topEnd = 14.dp,
                    bottomStart = if (message.isMine) 14.dp else 5.dp,
                    bottomEnd = if (message.isMine) 5.dp else 14.dp,
                ),
            )
            .padding(14.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.widthIn(min = 310.dp, max = 390.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (message.isMine) {
                            Color.White.copy(alpha = 0.22f)
                        } else {
                            PosterPrimary.copy(alpha = 0.18f)
                        },
                        shape = RoundedCornerShape(10.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Description,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp),
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = attachment.fileName,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = attachment.fileSize,
                    color = if (message.isMine) Color(0xFFE0DFFF) else PosterTextMuted,
                    fontSize = 12.sp,
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                imageVector = Icons.Outlined.Download,
                contentDescription = "Download",
                tint = Color.White,
                modifier = Modifier.size(22.dp),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = message.time,
            color = if (message.isMine) Color(0xFFE0DFFF) else PosterTextMuted,
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun ImageMessageBubble(
    message: MessageUi,
) {
    val attachment = message.attachment ?: return
    val context = LocalContext.current
    val imageModel = remember(context, attachment.localUri, attachment.remoteUrl) {
        attachment.localUri ?: attachment.remoteUrl?.let { remoteUrl ->
            RemoteImageConfig.buildImageModel(
                context = context,
                remoteUrl = remoteUrl,
            )
        }
    }
    val bubbleShape = RoundedCornerShape(
        topStart = 14.dp,
        topEnd = 14.dp,
        bottomStart = if (message.isMine) 14.dp else 5.dp,
        bottomEnd = if (message.isMine) 5.dp else 14.dp,
    )

    Column(
        modifier = Modifier
            .background(
                color = if (message.isMine) PosterPrimary else PosterSurface,
                shape = bubbleShape,
            )
            .clip(bubbleShape),
    ) {
        if (imageModel != null) {
            AsyncImage(
                model = imageModel,
                contentDescription = attachment.fileName,
                modifier = Modifier
                    .width(190.dp)
                    .height(255.dp),
                contentScale = ContentScale.Crop,
            )
        } else {
            Box(
                modifier = Modifier
                    .width(190.dp)
                    .height(255.dp)
                    .background(PosterSurface),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Image,
                    contentDescription = null,
                    tint = PosterTextMuted,
                    modifier = Modifier.size(36.dp),
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (message.isMine) PosterPrimary else PosterSurface)
                .padding(horizontal = 14.dp, vertical = 10.dp),
        ) {
            Text(
                text = message.time,
                color = if (message.isMine) Color(0xFFE0DFFF) else PosterTextMuted,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
private fun MessageInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onAttachFileClick: () -> Unit,
    onAttachImageClick: () -> Unit,
    onSendClick: () -> Unit,
    enabled: Boolean = true,
) {
    val canSend = enabled && value.trim().isNotEmpty()
    var sendPulse by remember { mutableStateOf(0) }
    val sendScale = remember { Animatable(1f) }

    LaunchedEffect(sendPulse) {
        if (sendPulse == 0) {
            return@LaunchedEffect
        }

        sendScale.snapTo(0.84f)
        sendScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium,
            ),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .navigationBarsPadding()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 14.dp),
    ) {
        if (!enabled) {
            Text(
                text = "Сообщения нельзя отправлять без mail access token. Немного грустно, но так безопаснее.",
                color = PosterTextMuted,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(bottom = 10.dp),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FloatingInputAction(
                imageVector = Icons.Outlined.AttachFile,
                contentDescription = "Attach file",
                enabled = enabled,
                onClick = onAttachFileClick,
            )

            Spacer(modifier = Modifier.width(8.dp))

            FloatingInputAction(
                imageVector = Icons.Outlined.Image,
                contentDescription = "Attach image",
                enabled = enabled,
                onClick = onAttachImageClick,
            )

            Spacer(modifier = Modifier.width(10.dp))

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                singleLine = true,
                cursorBrush = SolidColor(PosterPrimary),
                textStyle = TextStyle(
                    color = if (enabled) PosterTextPrimary else PosterTextMuted,
                    fontSize = 16.sp,
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .shadow(
                                elevation = 22.dp,
                                shape = RoundedCornerShape(25.dp),
                                spotColor = PosterPrimary.copy(alpha = 0.14f),
                            )
                            .background(
                                color = PosterSurface.copy(alpha = 0.9f),
                                shape = RoundedCornerShape(25.dp),
                            )
                            .border(
                                width = 1.dp,
                                color = PosterStroke.copy(alpha = 0.55f),
                                shape = RoundedCornerShape(25.dp),
                            )
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = if (enabled) "Type a message..." else "Token required...",
                                color = PosterTextMuted,
                                fontSize = 16.sp,
                            )
                        }

                        innerTextField()
                    }
                },
                modifier = Modifier.weight(1f),
            )

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .graphicsLayer {
                        scaleX = sendScale.value
                        scaleY = sendScale.value
                    }
                    .shadow(
                        elevation = if (canSend) 24.dp else 0.dp,
                        shape = CircleShape,
                        spotColor = PosterPrimary.copy(alpha = 0.32f),
                    )
                    .background(
                        brush = Brush.linearGradient(
                            if (canSend) {
                                listOf(PosterPrimary, PosterPrimaryDark)
                            } else {
                                listOf(Color(0xFF312A7A), Color(0xFF261D65))
                            },
                        ),
                        shape = CircleShape,
                    )
                    .clickable(enabled = canSend) {
                        sendPulse += 1
                        onSendClick()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Send,
                    contentDescription = "Send",
                    tint = if (canSend) Color.White else PosterTextMuted,
                    modifier = Modifier.size(23.dp),
                )
            }
        }
    }
}

@Composable
private fun FloatingInputAction(
    imageVector: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .shadow(
                elevation = 18.dp,
                shape = CircleShape,
                spotColor = PosterPrimary.copy(alpha = 0.16f),
            )
            .background(
                color = PosterSurface.copy(alpha = 0.92f),
                shape = CircleShape,
            )
            .border(
                width = 1.dp,
                color = PosterStroke.copy(alpha = 0.55f),
                shape = CircleShape,
            )
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = if (enabled) PosterIcon else PosterTextMuted,
            modifier = Modifier.size(22.dp),
        )
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun MessagesScreenPreview() {
    MessagesScreen(
        contactName = "Alice Johnson",
        contactInitials = "AJ",
        messages = emptyList(),
        onBackClick = {},
        onProfileClick = {},
        onMoreClick = {},
        onSendClick = {},
    )
}
