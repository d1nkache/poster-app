package com.example.poster.presentation.messages

import android.content.Context
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Description
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poster.domain.model.Attachment
import com.example.poster.domain.model.AttachmentType
import com.example.poster.domain.model.AttachmentUploadStatus

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

data class MessageUi(
    val id: String,
    val text: String,
    val time: String,
    val isMine: Boolean,
    val attachments: List<Attachment> = emptyList(),
)

val sampleMessages = listOf(
    MessageUi(
        id = "1",
        text = "Hey! How are you?",
        time = "10:00 AM",
        isMine = false,
    ),
    MessageUi(
        id = "2",
        text = "I'm good, thanks! Just finished the project.",
        time = "10:05 AM",
        isMine = true,
    ),
    MessageUi(
        id = "3",
        text = "That's great! Can you send me the files?",
        time = "10:06 AM",
        isMine = false,
    ),
    MessageUi(
        id = "4",
        text = "Sure, I'll send them in a few minutes.",
        time = "10:08 AM",
        isMine = true,
    ),
    MessageUi(
        id = "5",
        text = "Thanks! Looking forward to it.",
        time = "10:10 AM",
        isMine = false,
    ),
)

@Composable
fun MessagesScreen(
    contactName: String = "Alice Johnson",
    contactInitials: String = "AJ",
    messages: List<MessageUi> = sampleMessages,
    canSendMessages: Boolean = true,
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
    onSendClick: (String, List<Attachment>) -> Unit = { _, _ -> },
) {
    var input by remember { mutableStateOf("") }
    var selectedAttachments by remember { mutableStateOf<List<Attachment>>(emptyList()) }
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val lastMessageId = messages.lastOrNull()?.id
    val attachmentPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        if (uri != null) {
            selectedAttachments = selectedAttachments + uri.toLocalAttachment(context)
        }
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
        ChatWallpaper(
            modifier = Modifier.fillMaxSize(),
        )

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
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
                    Spacer(modifier = Modifier.height(22.dp))
                }
            }

            MessageInputBar(
                value = input,
                selectedAttachments = selectedAttachments,
                onValueChange = { input = it },
                enabled = canSendMessages,
                onAttachClick = {
                    attachmentPicker.launch(arrayOf("*/*"))
                },
                onRemoveAttachmentClick = { attachment ->
                    selectedAttachments = selectedAttachments.filterNot { it.id == attachment.id }
                },
                onSendClick = {
                    val text = input.trim()
                    if (canSendMessages && (text.isNotEmpty() || selectedAttachments.isNotEmpty())) {
                        onSendClick(text, selectedAttachments)
                        input = ""
                        selectedAttachments = emptyList()
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PosterTopBar)
            .statusBarsPadding()
            .height(74.dp)
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

        Spacer(modifier = Modifier.width(14.dp))

        Box(
            modifier = Modifier
                .size(40.dp)
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
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onProfileClick() },
        ) {
            Text(
                text = contactName,
                color = PosterTextPrimary,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "Online",
                color = PosterTextSecondary,
                fontSize = 14.sp,
            )
        }

        Icon(
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = "More",
            tint = PosterTextSecondary,
            modifier = Modifier
                .size(26.dp)
                .clickable { onMoreClick() },
        )
    }
}

@Composable
private fun MessageBubble(
    message: MessageUi,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isMine) Arrangement.End else Arrangement.Start,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 330.dp)
                .background(
                    color = if (message.isMine) PosterPrimary else PosterSurface,
                    shape = RoundedCornerShape(
                        topStart = 14.dp,
                        topEnd = 14.dp,
                        bottomStart = if (message.isMine) 14.dp else 5.dp,
                        bottomEnd = if (message.isMine) 5.dp else 14.dp,
                    ),
                )
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 13.dp,
                    bottom = 11.dp,
                ),
        ) {
            if (message.attachments.isNotEmpty()) {
                message.attachments.forEach { attachment ->
                    MessageAttachmentView(attachment = attachment)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            if (message.text.isNotBlank()) {
                Text(
                    text = message.text,
                    color = PosterTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 21.sp,
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
}

@Composable
private fun MessageInputBar(
    value: String,
    selectedAttachments: List<Attachment>,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    onAttachClick: () -> Unit,
    onRemoveAttachmentClick: (Attachment) -> Unit,
    onSendClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PosterTopBar)
            .border(width = 0.5.dp, color = PosterDivider)
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
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

        if (selectedAttachments.isNotEmpty()) {
            SelectedAttachmentsPreview(
                attachments = selectedAttachments,
                onRemoveAttachmentClick = onRemoveAttachmentClick,
                modifier = Modifier.padding(bottom = 10.dp),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color = PosterSurface,
                        shape = CircleShape,
                    )
                    .border(
                        width = 1.dp,
                        color = PosterStroke.copy(alpha = 0.55f),
                        shape = CircleShape,
                    )
                    .clickable(enabled = enabled) { onAttachClick() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.AttachFile,
                    contentDescription = "Attach file",
                    tint = if (enabled) PosterTextSecondary else PosterTextMuted,
                    modifier = Modifier.size(23.dp),
                )
            }

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
                            .background(
                                color = PosterSurface,
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
                                text = if (enabled) {
                                    "Type a message..."
                                } else {
                                    "Token required..."
                                },
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
                    .background(
                        brush = Brush.linearGradient(
                            if (enabled) {
                                listOf(PosterPrimary, PosterPrimaryDark)
                            } else {
                                listOf(Color(0xFF312A7A), Color(0xFF261D65))
                            },
                        ),
                        shape = CircleShape,
                    )
                    .clickable(enabled = enabled) { onSendClick() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Send,
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier.size(23.dp),
                )
            }
        }
    }
}

@Composable
private fun SelectedAttachmentsPreview(
    attachments: List<Attachment>,
    onRemoveAttachmentClick: (Attachment) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        attachments.forEach { attachment ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = PosterSurface,
                        shape = RoundedCornerShape(12.dp),
                    )
                    .border(
                        width = 1.dp,
                        color = PosterStroke.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp),
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AttachmentIcon(type = attachment.type)

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = attachment.fileName,
                        color = PosterTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = attachment.mimeType,
                        color = PosterTextMuted,
                        fontSize = 12.sp,
                    )
                }

                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Remove attachment",
                    tint = PosterTextSecondary,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onRemoveAttachmentClick(attachment) },
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun MessageAttachmentView(attachment: Attachment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.Black.copy(alpha = 0.14f),
                shape = RoundedCornerShape(10.dp),
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AttachmentIcon(type = attachment.type)

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = attachment.fileName,
                color = PosterTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = attachment.uploadStatus.name.lowercase().replace("_", " "),
                color = PosterTextSecondary,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
private fun AttachmentIcon(type: AttachmentType) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .background(
                color = PosterPrimary.copy(alpha = 0.22f),
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = if (type == AttachmentType.IMAGE || type == AttachmentType.VIDEO) {
                Icons.Outlined.Image
            } else {
                Icons.Outlined.Description
            },
            contentDescription = null,
            tint = Color(0xFFB7BCFF),
            modifier = Modifier.size(19.dp),
        )
    }
}

private fun android.net.Uri.toLocalAttachment(context: Context): Attachment {
    val uriText = toString()
    val contentResolver = context.contentResolver
    val mimeType = contentResolver.getType(this).orEmpty().ifBlank {
        "application/octet-stream"
    }
    var fileName = lastPathSegment
        ?.substringAfterLast('/')
        ?.takeIf { it.isNotBlank() }
        ?: "selected-file"
    var sizeBytes = 0L

    contentResolver.query(this, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
        if (cursor.moveToFirst()) {
            if (nameIndex >= 0) {
                fileName = cursor.getString(nameIndex) ?: fileName
            }
            if (sizeIndex >= 0) {
                sizeBytes = cursor.getLong(sizeIndex)
            }
        }
    }

    return Attachment(
        id = "local-${System.currentTimeMillis()}",
        localUri = uriText,
        remoteUrl = null,
        fileName = fileName,
        mimeType = mimeType,
        sizeBytes = sizeBytes,
        type = mimeType.toAttachmentType(),
        uploadStatus = AttachmentUploadStatus.LOCAL_ONLY,
    )
}

private fun String.toAttachmentType(): AttachmentType {
    return when {
        startsWith("image/") -> AttachmentType.IMAGE
        startsWith("video/") -> AttachmentType.VIDEO
        startsWith("audio/") -> AttachmentType.AUDIO
        contains("pdf") || contains("document") || contains("text") -> AttachmentType.DOCUMENT
        else -> AttachmentType.UNKNOWN
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun MessagesScreenPreview() {
    MessagesScreen(
        contactName = "Alice Johnson",
        contactInitials = "AJ",
        messages = sampleMessages,
        onBackClick = {},
        onProfileClick = {},
        onMoreClick = {},
        onSendClick = { _, _ -> },
    )
}
