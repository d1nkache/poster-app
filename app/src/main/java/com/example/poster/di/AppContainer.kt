package com.example.poster.di

import com.example.poster.data.local.InMemoryPosterLocalDataSource
import com.example.poster.data.local.PosterLocalDataSource
import com.example.poster.data.remote.PosterRemoteDataSource
import com.example.poster.data.remote.StubPosterRemoteDataSource
import com.example.poster.data.repository.AuthRepositoryImpl
import com.example.poster.data.repository.ChatRepositoryImpl
import com.example.poster.data.repository.MessageRepositoryImpl
import com.example.poster.data.repository.ProfileRepositoryImpl
import com.example.poster.domain.repository.AuthRepository
import com.example.poster.domain.repository.ChatRepository
import com.example.poster.domain.repository.MessageRepository
import com.example.poster.domain.repository.ProfileRepository
import com.example.poster.domain.usecase.GetProfileUseCase
import com.example.poster.domain.usecase.LoginUseCase
import com.example.poster.domain.usecase.ObserveChatsUseCase
import com.example.poster.domain.usecase.ObserveMessagesUseCase
import com.example.poster.domain.usecase.RegisterUseCase
import com.example.poster.domain.usecase.SendMessageUseCase
import com.example.poster.domain.usecase.UpdateProfileUseCase
import com.example.poster.domain.usecase.VerifyOtpUseCase

object AppContainer {
    private val remoteDataSource: PosterRemoteDataSource = StubPosterRemoteDataSource()
    private val localDataSource: PosterLocalDataSource = InMemoryPosterLocalDataSource()

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
        )
    }

    val chatRepository: ChatRepository by lazy {
        ChatRepositoryImpl(remoteDataSource)
    }

    val messageRepository: MessageRepository by lazy {
        MessageRepositoryImpl(remoteDataSource)
    }

    val profileRepository: ProfileRepository by lazy {
        ProfileRepositoryImpl(remoteDataSource)
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
}
