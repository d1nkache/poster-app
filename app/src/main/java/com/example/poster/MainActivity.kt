package com.example.poster

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Density
import com.example.poster.di.AppContainer
import com.example.poster.domain.model.Attachment
import com.example.poster.domain.model.AttachmentType
import com.example.poster.domain.model.AttachmentUploadStatus
import com.example.poster.domain.model.Profile
import com.example.poster.presentation.auth.otp.VerifyEmailScreen
import com.example.poster.presentation.auth.register.RegisterScreen
import com.example.poster.presentation.auth.signin.SignInScreen
import com.example.poster.presentation.chats.ChatListScreen
import com.example.poster.presentation.chats.ChatPreviewUi
import com.example.poster.presentation.chats.toChatPreviewUi
import com.example.poster.presentation.messages.MessageContentType
import com.example.poster.presentation.messages.MessageUi
import com.example.poster.presentation.messages.MessagesScreen
import com.example.poster.presentation.messages.toMessageUi
import com.example.poster.presentation.profile.MyProfileScreen
import com.example.poster.presentation.profile.ProfileFileUi
import com.example.poster.presentation.profile.ProfileScreen
import com.example.poster.presentation.settings.AccessTokenGuideScreen
import com.example.poster.presentation.settings.AccessTokenSettingsScreen
import com.example.poster.presentation.settings.LanguageSettingsScreen
import com.example.poster.presentation.settings.PrivacySettingsScreen
import com.example.poster.presentation.settings.PrivacySettingsUi
import com.example.poster.presentation.settings.SettingsScreen
import com.example.poster.presentation.settings.SettingsTextEditScreen
import com.example.poster.ui.theme.PosterTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "PosterMainActivity"
        private const val UI_SCALE = 0.85f
    }

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate: app started")
        enableEdgeToEdge()

        setContent {
            PosterTheme {
                val baseDensity = LocalDensity.current

                CompositionLocalProvider(
                    LocalDensity provides Density(
                        density = baseDensity.density * UI_SCALE,
                        fontScale = baseDensity.fontScale,
                    )
                ) {
                /*
                 * PLACEHOLDER AUTH LOGIC
                 *
                 * Сейчас всегда false.
                 * Поэтому приложение по дефолту открывает Sign In / Sign Up.
                 *
                 * Потом здесь будет проверка:
                 * - есть ли JWT access token
                 * - не истёк ли токен
                 * - нужно ли отправлять пользователя на Login/Register
                */
                val isAuthorized = false
                val scope = rememberCoroutineScope()

                var currentScreen by rememberSaveable {
                    mutableStateOf(
                        if (isAuthorized) {
                            AppScreen.CHATS
                        } else {
                            AppScreen.SIGN_IN
                        }
                    )
                }
                var verificationEmail by rememberSaveable {
                    mutableStateOf("user@mail.ru")
                }
                var selectedChat by remember {
                    mutableStateOf<ChatPreviewUi?>(null)
                }
                var chats by remember {
                    mutableStateOf<List<ChatPreviewUi>>(emptyList())
                }
                var messages by remember {
                    mutableStateOf<List<MessageUi>>(emptyList())
                }
                var myProfile by remember {
                    mutableStateOf<Profile?>(null)
                }
                var accessTokenBackScreen by rememberSaveable {
                    mutableStateOf(AppScreen.CHATS)
                }
                var myProfileBackScreen by rememberSaveable {
                    mutableStateOf(AppScreen.SETTINGS)
                }
                var hasMailAccessToken by rememberSaveable {
                    mutableStateOf(false)
                }
                var settingsEmail by rememberSaveable {
                    mutableStateOf("your.email@example.com")
                }
                var settingsBirthday by rememberSaveable {
                    mutableStateOf("January 1, 2000")
                }
                var settingsLanguage by rememberSaveable {
                    mutableStateOf("English")
                }
                var currentEditableSetting by rememberSaveable {
                    mutableStateOf(EditableSetting.NAME)
                }
                var showOnlineStatus by rememberSaveable {
                    mutableStateOf(true)
                }
                var sendReadReceipts by rememberSaveable {
                    mutableStateOf(true)
                }
                var confirmBeforeOpeningFiles by rememberSaveable {
                    mutableStateOf(true)
                }
                val avatarPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.OpenDocument(),
                ) { uri ->
                    uri ?: return@rememberLauncherForActivityResult

                    runCatching {
                        contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION,
                        )
                    }

                    scope.launch {
                        AppContainer.updateAvatarUseCase(uri.toString())
                            .onSuccess { updatedProfile ->
                                myProfile = updatedProfile
                            }
                            .onFailure { error ->
                                Log.e(TAG, "Failed to update avatar", error)
                            }
                    }
                }

                LaunchedEffect(Unit) {
                    Log.d(TAG, "Compose content loaded")
                    Log.d(TAG, "Auth placeholder check started")
                    Log.d(TAG, "isAuthorized = $isAuthorized")

                    AppContainer.observeChatsUseCase().collect { loadedChats ->
                        chats = loadedChats.map { chat -> chat.toChatPreviewUi() }
                    }
                }

                LaunchedEffect(Unit) {
                    hasMailAccessToken = AppContainer.checkMailAccessTokenUseCase()

                    AppContainer.getMyProfileUseCase()
                        .onSuccess { profile ->
                            myProfile = profile
                            if (settingsEmail == "your.email@example.com") {
                                settingsEmail = profile.email
                            }
                        }
                        .onFailure { error ->
                            Log.e(TAG, "Failed to load my profile", error)
                        }
                }

                LaunchedEffect(currentScreen) {
                    Log.d(TAG, "Current screen = $currentScreen")
                }

                LaunchedEffect(selectedChat?.id) {
                    val chatId = selectedChat?.id ?: return@LaunchedEffect
                    messages = emptyList()

                    AppContainer.observeDomainMessagesUseCase(chatId).collect { loadedMessages ->
                        messages = loadedMessages.map { message -> message.toMessageUi() }
                    }
                }

                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = {
                        val direction = if (targetState.animationOrder >= initialState.animationOrder) {
                            1
                        } else {
                            -1
                        }

                        (
                            slideInHorizontally(
                                animationSpec = tween(durationMillis = 260),
                                initialOffsetX = { fullWidth -> fullWidth / 4 * direction },
                            ) + fadeIn(animationSpec = tween(durationMillis = 220)) +
                                scaleIn(
                                    initialScale = 0.98f,
                                    animationSpec = tween(durationMillis = 260),
                                )
                            ).togetherWith(
                                slideOutHorizontally(
                                    animationSpec = tween(durationMillis = 220),
                                    targetOffsetX = { fullWidth -> -fullWidth / 5 * direction },
                                ) + fadeOut(animationSpec = tween(durationMillis = 180)) +
                                    scaleOut(
                                        targetScale = 0.98f,
                                        animationSpec = tween(durationMillis = 220),
                                    )
                            )
                    },
                    label = "posterScreenTransition",
                ) { screen ->
                when (screen) {
                    AppScreen.SIGN_IN -> {
                        SignInScreen(
                            onSignInClick = { email, _ ->
                                Log.d(TAG, "Sign In clicked: email=$email")
                                Log.d(TAG, "Placeholder sign in success, opening chats")
                                currentScreen = AppScreen.CHATS
                            },
                            onSignUpClick = {
                                Log.d(TAG, "Navigate to Sign Up")
                                currentScreen = AppScreen.SIGN_UP
                            },
                        )
                    }

                    AppScreen.SIGN_UP -> {
                        RegisterScreen(
                            onContinueClick = { name, email, _ ->
                                Log.d(TAG, "Sign Up clicked: name=$name, email=$email")
                                verificationEmail = email.ifBlank { "user@mail.ru" }
                                Log.d(TAG, "Opening VerifyEmailScreen for $verificationEmail")
                                currentScreen = AppScreen.VERIFY_EMAIL
                            },
                            onSignInClick = {
                                Log.d(TAG, "Navigate to Sign In")
                                currentScreen = AppScreen.SIGN_IN
                            },
                        )
                    }

                    AppScreen.VERIFY_EMAIL -> {
                        VerifyEmailScreen(
                            email = verificationEmail,
                            onVerifyClick = { code ->
                                Log.d(TAG, "Verify code clicked: code=$code")
                                Log.d(TAG, "Placeholder verification success, opening chats")
                                currentScreen = AppScreen.CHATS
                            },
                            onResendClick = {
                                Log.d(TAG, "Resend code clicked for $verificationEmail")
                            },
                        )
                    }

                    AppScreen.CHATS -> {
                        ChatListScreen(
                            chats = chats,
                            isSetupRequired = !hasMailAccessToken,
                            profileAvatarUrl = myProfile?.avatarUrl,
                            hasMailAccessToken = hasMailAccessToken,
                            onChatClick = { chat ->
                                if (hasMailAccessToken) {
                                    Log.d(TAG, "Chat clicked: id=${chat.id}, name=${chat.name}")
                                    selectedChat = chat
                                    currentScreen = AppScreen.MESSAGES
                                } else {
                                    Log.d(TAG, "Chat blocked, mail access token is missing")
                                }
                            },
                            onSetupTokenClick = {
                                Log.d(TAG, "Setup token clicked")
                                accessTokenBackScreen = AppScreen.CHATS
                                currentScreen = AppScreen.ACCESS_TOKEN_SETTINGS
                            },
                            onSettingsClick = {
                                Log.d(TAG, "Settings clicked")
                                currentScreen = AppScreen.SETTINGS
                            },
                            onProfileClick = {
                                Log.d(TAG, "Open MyProfile from Chats")
                                myProfileBackScreen = AppScreen.CHATS
                                currentScreen = AppScreen.MY_PROFILE
                            },
                        )
                    }

                    AppScreen.SETTINGS -> {
                        val profile = myProfile

                        SettingsScreen(
                            name = profile?.name ?: "Your Name",
                            username = profile?.username ?: "@your.username",
                            email = settingsEmail,
                            birthday = settingsBirthday,
                            bio = profile?.bio ?: "Hey there! I'm using Poster.",
                            language = settingsLanguage,
                            accessTokenStatus = if (hasMailAccessToken) {
                                "Configured"
                            } else {
                                "Not configured"
                            },
                            onBackClick = {
                                Log.d(TAG, "Back from Settings")
                                currentScreen = AppScreen.CHATS
                            },
                            onAccessTokenClick = {
                                Log.d(TAG, "Open Access Token Settings")
                                accessTokenBackScreen = AppScreen.SETTINGS
                                currentScreen = AppScreen.ACCESS_TOKEN_SETTINGS
                            },
                            onPrivacyClick = {
                                Log.d(TAG, "Privacy & Security clicked")
                                currentScreen = AppScreen.PRIVACY_SETTINGS
                            },
                            onLanguageClick = {
                                Log.d(TAG, "Language clicked")
                                currentScreen = AppScreen.LANGUAGE_SETTINGS
                            },
                            onProfilePhotoClick = {
                                Log.d(TAG, "Profile photo clicked")
                                avatarPickerLauncher.launch(arrayOf("image/*"))
                            },
                            onAccountItemClick = { itemId ->
                                Log.d(TAG, "Account item clicked: $itemId")
                                EditableSetting.fromId(itemId)?.let { editableSetting ->
                                    currentEditableSetting = editableSetting
                                    currentScreen = AppScreen.SETTINGS_TEXT_EDIT
                                }
                            },
                        )
                    }

                    AppScreen.SETTINGS_TEXT_EDIT -> {
                        val profile = myProfile
                        val editableSetting = currentEditableSetting

                        SettingsTextEditScreen(
                            title = editableSetting.title,
                            label = editableSetting.title,
                            value = when (editableSetting) {
                                EditableSetting.NAME -> profile?.name ?: "Your Name"
                                EditableSetting.USERNAME -> profile?.username ?: "@your.username"
                                EditableSetting.EMAIL -> settingsEmail
                                EditableSetting.BIRTHDAY -> settingsBirthday
                                EditableSetting.BIO -> profile?.bio ?: "Hey there! I'm using Poster."
                            },
                            placeholder = editableSetting.placeholder,
                            singleLine = editableSetting != EditableSetting.BIO,
                            allowEmpty = editableSetting == EditableSetting.BIO,
                            keyboardType = editableSetting.keyboardType,
                            onBackClick = {
                                currentScreen = AppScreen.SETTINGS
                            },
                            onSaveClick = { value ->
                                Log.d(TAG, "Save setting ${editableSetting.id}: $value")
                                when (editableSetting) {
                                    EditableSetting.NAME -> {
                                        scope.launch {
                                            AppContainer.updateNameUseCase(value)
                                                .onSuccess { updatedProfile ->
                                                    myProfile = updatedProfile
                                                    currentScreen = AppScreen.SETTINGS
                                                }
                                                .onFailure { error ->
                                                    Log.e(TAG, "Failed to update name", error)
                                                }
                                        }
                                    }

                                    EditableSetting.USERNAME -> {
                                        val username = value.withUsernamePrefix()
                                        scope.launch {
                                            AppContainer.updateUsernameUseCase(username)
                                                .onSuccess { updatedProfile ->
                                                    myProfile = updatedProfile
                                                    currentScreen = AppScreen.SETTINGS
                                                }
                                                .onFailure { error ->
                                                    Log.e(TAG, "Failed to update username", error)
                                                }
                                        }
                                    }

                                    EditableSetting.EMAIL -> {
                                        settingsEmail = value
                                        currentScreen = AppScreen.SETTINGS
                                    }

                                    EditableSetting.BIRTHDAY -> {
                                        settingsBirthday = value
                                        currentScreen = AppScreen.SETTINGS
                                    }

                                    EditableSetting.BIO -> {
                                        scope.launch {
                                            AppContainer.updateBioUseCase(value)
                                                .onSuccess { updatedProfile ->
                                                    myProfile = updatedProfile
                                                    currentScreen = AppScreen.SETTINGS
                                                }
                                                .onFailure { error ->
                                                    Log.e(TAG, "Failed to update bio", error)
                                                }
                                        }
                                    }
                                }
                            },
                        )
                    }

                    AppScreen.LANGUAGE_SETTINGS -> {
                        LanguageSettingsScreen(
                            currentLanguage = settingsLanguage,
                            onBackClick = {
                                currentScreen = AppScreen.SETTINGS
                            },
                            onSaveLanguageClick = { language ->
                                Log.d(TAG, "Language saved: $language")
                                settingsLanguage = language
                                scope.launch {
                                    AppContainer.settingsRepository.changeLanguage(language)
                                        .onFailure { error ->
                                            Log.e(TAG, "Failed to save language", error)
                                        }
                                }
                                currentScreen = AppScreen.SETTINGS
                            },
                        )
                    }

                    AppScreen.PRIVACY_SETTINGS -> {
                        PrivacySettingsScreen(
                            settings = PrivacySettingsUi(
                                showOnlineStatus = showOnlineStatus,
                                sendReadReceipts = sendReadReceipts,
                                confirmBeforeOpeningFiles = confirmBeforeOpeningFiles,
                            ),
                            onBackClick = {
                                currentScreen = AppScreen.SETTINGS
                            },
                            onSavePrivacyClick = { settings ->
                                Log.d(TAG, "Privacy settings saved: $settings")
                                showOnlineStatus = settings.showOnlineStatus
                                sendReadReceipts = settings.sendReadReceipts
                                confirmBeforeOpeningFiles = settings.confirmBeforeOpeningFiles
                                currentScreen = AppScreen.SETTINGS
                            },
                        )
                    }

                    AppScreen.MY_PROFILE -> {
                        val profile = myProfile

                        MyProfileScreen(
                            name = profile?.name ?: "Your Name",
                            username = profile?.username ?: "@your.username",
                            bio = profile?.bio ?: "Hey there! I'm using Poster.",
                            avatarUrl = profile?.avatarUrl,
                            onBackClick = {
                                Log.d(TAG, "Back from MyProfile")
                                currentScreen = myProfileBackScreen
                            },
                            onAvatarClick = {
                                Log.d(TAG, "Avatar clicked from MyProfile")
                                avatarPickerLauncher.launch(arrayOf("image/*"))
                            },
                            onNameChange = { name ->
                                Log.d(TAG, "Name changed placeholder: $name")
                                scope.launch {
                                    AppContainer.updateNameUseCase(name)
                                        .onSuccess { updatedProfile ->
                                            myProfile = updatedProfile
                                        }
                                        .onFailure { error ->
                                            Log.e(TAG, "Failed to update name", error)
                                        }
                                }
                            },
                            onUsernameChange = { username ->
                                Log.d(TAG, "Username changed placeholder: $username")
                                scope.launch {
                                    AppContainer.updateUsernameUseCase(username)
                                        .onSuccess { updatedProfile ->
                                            myProfile = updatedProfile
                                        }
                                        .onFailure { error ->
                                            Log.e(TAG, "Failed to update username", error)
                                        }
                                }
                            },
                            onBioChange = { bio ->
                                Log.d(TAG, "Bio changed placeholder, length=${bio.length}")
                                scope.launch {
                                    AppContainer.updateBioUseCase(bio)
                                        .onSuccess { updatedProfile ->
                                            myProfile = updatedProfile
                                        }
                                        .onFailure { error ->
                                            Log.e(TAG, "Failed to update bio", error)
                                        }
                                }
                            },
                        )
                    }

                    AppScreen.ACCESS_TOKEN_SETTINGS -> {
                        AccessTokenSettingsScreen(
                            initialToken = "",
                            onBackClick = {
                                Log.d(TAG, "Back from AccessTokenSettings")
                                currentScreen = accessTokenBackScreen
                            },
                            onSaveTokenClick = { token ->
                                Log.d(TAG, "Access token save requested, length=${token.length}")
                                scope.launch {
                                    AppContainer.saveMailAccessTokenUseCase(token)
                                        .onSuccess {
                                            hasMailAccessToken = true
                                            currentScreen = accessTokenBackScreen
                                        }
                                        .onFailure { error ->
                                            Log.e(TAG, "Failed to save access token", error)
                                        }
                                }
                            },
                            onHowToGetTokenClick = {
                                Log.d(TAG, "How to get access token clicked")
                                currentScreen = AppScreen.ACCESS_TOKEN_HELP
                            },
                        )
                    }

                    AppScreen.ACCESS_TOKEN_HELP -> {
                        AccessTokenGuideScreen(
                            onBackClick = {
                                currentScreen = AppScreen.ACCESS_TOKEN_SETTINGS
                            },
                        )
                    }

                    AppScreen.MESSAGES -> {
                        val chat = selectedChat ?: chats.firstOrNull()

                        if (chat == null) {
                            LaunchedEffect(Unit) {
                                currentScreen = AppScreen.CHATS
                            }
                        } else {
                            MessagesScreen(
                                contactName = chat.name,
                                contactInitials = chat.initials,
                                messages = messages,
                                canSendMessages = hasMailAccessToken,
                                onBackClick = {
                                    Log.d(TAG, "Back from Messages")
                                    currentScreen = AppScreen.CHATS
                                },
                                onProfileClick = {
                                    Log.d(TAG, "Open Profile from Messages")
                                    currentScreen = AppScreen.PROFILE
                                },
                                onMoreClick = {
                                    Log.d(TAG, "More clicked in Messages")
                                },
                                onSendClick = { text ->
                                    if (hasMailAccessToken) {
                                        Log.d(TAG, "Send message: $text")
                                        scope.launch {
                                            AppContainer.sendDomainMessageUseCase(chat.id, text)
                                                .onFailure { error ->
                                                    Log.e(TAG, "Failed to send message", error)
                                                }
                                        }
                                    } else {
                                        Log.d(TAG, "Send message blocked, mail access token is missing")
                                    }
                                },
                                onFilePicked = { uri ->
                                    Log.d(TAG, "File picked: $uri")
                                    scope.launch {
                                        val attachment = uri.toAttachment(
                                            context = this@MainActivity,
                                            type = AttachmentType.DOCUMENT,
                                            fallbackName = "Selected_File.pdf",
                                            fallbackMimeType = "application/pdf",
                                        )

                                        AppContainer.sendMessageWithAttachmentsUseCase(
                                            chatId = chat.id,
                                            text = "",
                                            attachments = listOf(attachment),
                                        ).onFailure { error ->
                                            Log.e(TAG, "Failed to send file attachment", error)
                                        }
                                    }
                                },
                                onImagePicked = { uri ->
                                    Log.d(TAG, "Image picked: $uri")
                                    scope.launch {
                                        val attachment = uri.toAttachment(
                                            context = this@MainActivity,
                                            type = AttachmentType.IMAGE,
                                            fallbackName = "image.jpg",
                                            fallbackMimeType = "image/jpeg",
                                        )

                                        AppContainer.sendMessageWithAttachmentsUseCase(
                                            chatId = chat.id,
                                            text = "",
                                            attachments = listOf(attachment),
                                        ).onFailure { error ->
                                            Log.e(TAG, "Failed to send image attachment", error)
                                        }
                                    }
                                },
                            )
                        }
                    }

                    AppScreen.PROFILE -> {
                        val chat = selectedChat ?: chats.firstOrNull()

                        if (chat == null) {
                            LaunchedEffect(Unit) {
                                currentScreen = AppScreen.CHATS
                            }
                        } else {
                            ProfileScreen(
                                initials = chat.initials,
                                name = chat.name,
                                username = "@${chat.name.lowercase().replace(" ", ".")}",
                                bio = "Product designer passionate about creating beautiful and functional user experiences.",
                                attachments = messages.toProfileMediaAttachments(),
                                files = messages.toProfileFiles(),
                                onBackClick = {
                                    Log.d(TAG, "Back from Profile")
                                    currentScreen = AppScreen.MESSAGES
                                },
                            )
                        }
                    }
                }
                }
            }
        }
    }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}

private enum class AppScreen {
    SIGN_IN,
    SIGN_UP,
    VERIFY_EMAIL,
    CHATS,
    SETTINGS,
    SETTINGS_TEXT_EDIT,
    LANGUAGE_SETTINGS,
    PRIVACY_SETTINGS,
    MY_PROFILE,
    ACCESS_TOKEN_SETTINGS,
    ACCESS_TOKEN_HELP,
    MESSAGES,
    PROFILE,
}

private val AppScreen.animationOrder: Int
    get() = when (this) {
        AppScreen.SIGN_IN -> 0
        AppScreen.SIGN_UP -> 1
        AppScreen.VERIFY_EMAIL -> 2
        AppScreen.CHATS -> 3
        AppScreen.SETTINGS -> 4
        AppScreen.SETTINGS_TEXT_EDIT -> 5
        AppScreen.LANGUAGE_SETTINGS -> 5
        AppScreen.PRIVACY_SETTINGS -> 5
        AppScreen.MY_PROFILE -> 5
        AppScreen.ACCESS_TOKEN_SETTINGS -> 5
        AppScreen.ACCESS_TOKEN_HELP -> 6
        AppScreen.MESSAGES -> 7
        AppScreen.PROFILE -> 8
    }

private enum class EditableSetting(
    val id: String,
    val title: String,
    val placeholder: String,
    val keyboardType: KeyboardType = KeyboardType.Text,
) {
    NAME(
        id = "name",
        title = "Name",
        placeholder = "Enter your name",
    ),
    USERNAME(
        id = "username",
        title = "Username",
        placeholder = "Enter your username",
    ),
    EMAIL(
        id = "email",
        title = "Email",
        placeholder = "Enter your email",
        keyboardType = KeyboardType.Email,
    ),
    BIRTHDAY(
        id = "birthday",
        title = "Birthday",
        placeholder = "Enter your birthday",
    ),
    BIO(
        id = "bio",
        title = "Bio",
        placeholder = "Tell people about yourself",
    );

    companion object {
        fun fromId(id: String): EditableSetting? {
            return entries.firstOrNull { it.id == id }
        }
    }
}

private fun String.withUsernamePrefix(): String {
    val username = trim()
    return if (username.startsWith("@")) {
        username
    } else {
        "@$username"
    }
}

private fun List<MessageUi>.toProfileFiles(): List<ProfileFileUi> {
    return mapNotNull { message ->
        val attachment = message.attachment ?: return@mapNotNull null
        if (message.type != MessageContentType.FILE) {
            return@mapNotNull null
        }

        ProfileFileUi(
            id = attachment.id,
            name = attachment.fileName,
            size = attachment.fileSize,
            date = message.time,
        )
    }
}

private fun List<MessageUi>.toProfileMediaAttachments(): List<Attachment> {
    return mapNotNull { message ->
        val attachment = message.attachment ?: return@mapNotNull null
        if (message.type != MessageContentType.IMAGE) {
            return@mapNotNull null
        }

        Attachment(
            id = attachment.id,
            localUri = attachment.localUri,
            remoteUrl = attachment.remoteUrl,
            type = AttachmentType.IMAGE,
            fileName = attachment.fileName,
            mimeType = attachment.mimeType,
            sizeBytes = attachment.sizeBytes,
            uploadStatus = AttachmentUploadStatus.UPLOADED,
        )
    }
}

private fun Uri.toAttachment(
    context: Context,
    type: AttachmentType,
    fallbackName: String,
    fallbackMimeType: String,
): Attachment {
    val contentResolver = context.contentResolver
    var fileName = fallbackName
    var sizeBytes = 0L
    val mimeType = contentResolver.getType(this).orEmpty().ifBlank {
        fallbackMimeType
    }

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
        localUri = toString(),
        remoteUrl = null,
        type = type,
        fileName = fileName,
        mimeType = mimeType,
        sizeBytes = sizeBytes,
        uploadStatus = AttachmentUploadStatus.LOCAL_ONLY,
    )
}
