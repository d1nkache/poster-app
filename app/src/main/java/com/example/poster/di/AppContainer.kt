package com.example.poster.di

import com.example.poster.data.repository.MockAuthRepository
import com.example.poster.data.repository.MockChatRepository
import com.example.poster.data.repository.MockMessageRepository
import com.example.poster.data.repository.MockProfileRepository
import com.example.poster.data.repository.MockSettingsRepository
import com.example.poster.domain.repository.AuthRepository
import com.example.poster.domain.repository.ChatRepository
import com.example.poster.domain.repository.MessageRepository
import com.example.poster.domain.repository.ProfileRepository
import com.example.poster.domain.repository.SettingsRepository
import com.example.poster.domain.usecase.GetProfileUseCase
import com.example.poster.domain.usecase.LoginUseCase
import com.example.poster.domain.usecase.ObserveChatsUseCase
import com.example.poster.domain.usecase.ObserveMessagesUseCase
import com.example.poster.domain.usecase.RegisterUseCase
import com.example.poster.domain.usecase.SendMessageUseCase
import com.example.poster.domain.usecase.UpdateProfileUseCase
import com.example.poster.domain.usecase.VerifyOtpUseCase
import com.example.poster.domain.usecase.app.GetStartDestinationUseCase
import com.example.poster.domain.usecase.auth.CheckAuthStateUseCase
import com.example.poster.domain.usecase.auth.LogoutUseCase
import com.example.poster.domain.usecase.auth.SignInUseCase
import com.example.poster.domain.usecase.auth.SignUpUseCase
import com.example.poster.domain.usecase.auth.VerifyOtpUseCase as VerifyAuthOtpUseCase
import com.example.poster.domain.usecase.chat.GetChatsUseCase
import com.example.poster.domain.usecase.chat.SearchChatsUseCase
import com.example.poster.domain.usecase.message.GetMessagesUseCase
import com.example.poster.domain.usecase.message.ObserveMessagesUseCase as ObserveDomainMessagesUseCase
import com.example.poster.domain.usecase.message.SendMessageUseCase as SendDomainMessageUseCase
import com.example.poster.domain.usecase.profile.GetMyProfileUseCase
import com.example.poster.domain.usecase.profile.GetUserProfileUseCase
import com.example.poster.domain.usecase.profile.UpdateBioUseCase
import com.example.poster.domain.usecase.profile.UpdateNameUseCase
import com.example.poster.domain.usecase.profile.UpdateUsernameUseCase
import com.example.poster.domain.usecase.settings.CheckMailAccessTokenUseCase
import com.example.poster.domain.usecase.settings.GetSettingsUseCase
import com.example.poster.domain.usecase.settings.SaveMailAccessTokenUseCase

object AppContainer {
    val authRepository: AuthRepository by lazy {
        MockAuthRepository()
    }

    val chatRepository: ChatRepository by lazy {
        MockChatRepository()
    }

    val messageRepository: MessageRepository by lazy {
        MockMessageRepository()
    }

    val profileRepository: ProfileRepository by lazy {
        MockProfileRepository()
    }

    val settingsRepository: SettingsRepository by lazy {
        MockSettingsRepository()
    }

    val loginUseCase: LoginUseCase by lazy {
        LoginUseCase(authRepository)
    }

    val registerUseCase: RegisterUseCase by lazy {
        RegisterUseCase(authRepository)
    }

    val verifyOtpUseCase: VerifyOtpUseCase by lazy {
        VerifyOtpUseCase(authRepository)
    }

    val observeChatsUseCase: ObserveChatsUseCase by lazy {
        ObserveChatsUseCase(chatRepository)
    }

    val observeMessagesUseCase: ObserveMessagesUseCase by lazy {
        ObserveMessagesUseCase(messageRepository)
    }

    val sendMessageUseCase: SendMessageUseCase by lazy {
        SendMessageUseCase(messageRepository)
    }

    val getProfileUseCase: GetProfileUseCase by lazy {
        GetProfileUseCase(profileRepository)
    }

    val updateProfileUseCase: UpdateProfileUseCase by lazy {
        UpdateProfileUseCase(profileRepository)
    }

    val signInUseCase: SignInUseCase by lazy {
        SignInUseCase(authRepository)
    }

    val signUpUseCase: SignUpUseCase by lazy {
        SignUpUseCase(authRepository)
    }

    val checkAuthStateUseCase: CheckAuthStateUseCase by lazy {
        CheckAuthStateUseCase(authRepository)
    }

    val logoutUseCase: LogoutUseCase by lazy {
        LogoutUseCase(authRepository)
    }

    val verifyAuthOtpUseCase: VerifyAuthOtpUseCase by lazy {
        VerifyAuthOtpUseCase(authRepository)
    }

    val getStartDestinationUseCase: GetStartDestinationUseCase by lazy {
        GetStartDestinationUseCase(authRepository)
    }

    val getChatsUseCase: GetChatsUseCase by lazy {
        GetChatsUseCase(chatRepository)
    }

    val searchChatsUseCase: SearchChatsUseCase by lazy {
        SearchChatsUseCase(chatRepository)
    }

    val getMessagesUseCase: GetMessagesUseCase by lazy {
        GetMessagesUseCase(messageRepository)
    }

    val sendDomainMessageUseCase: SendDomainMessageUseCase by lazy {
        SendDomainMessageUseCase(messageRepository)
    }

    val observeDomainMessagesUseCase: ObserveDomainMessagesUseCase by lazy {
        ObserveDomainMessagesUseCase(messageRepository)
    }

    val getMyProfileUseCase: GetMyProfileUseCase by lazy {
        GetMyProfileUseCase(profileRepository)
    }

    val getUserProfileUseCase: GetUserProfileUseCase by lazy {
        GetUserProfileUseCase(profileRepository)
    }

    val updateUsernameUseCase: UpdateUsernameUseCase by lazy {
        UpdateUsernameUseCase(profileRepository)
    }

    val updateNameUseCase: UpdateNameUseCase by lazy {
        UpdateNameUseCase(profileRepository)
    }

    val updateBioUseCase: UpdateBioUseCase by lazy {
        UpdateBioUseCase(profileRepository)
    }

    val getSettingsUseCase: GetSettingsUseCase by lazy {
        GetSettingsUseCase(settingsRepository)
    }

    val saveMailAccessTokenUseCase: SaveMailAccessTokenUseCase by lazy {
        SaveMailAccessTokenUseCase(settingsRepository)
    }

    val checkMailAccessTokenUseCase: CheckMailAccessTokenUseCase by lazy {
        CheckMailAccessTokenUseCase(settingsRepository)
    }
}
