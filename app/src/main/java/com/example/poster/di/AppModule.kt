package com.example.poster.di

import android.content.Context
import com.example.poster.data.local.InMemoryPosterLocalDataSource
import com.example.poster.data.local.PosterLocalDataSource
import com.example.poster.data.remote.HttpPosterRemoteDataSource
import com.example.poster.data.remote.PosterRemoteDataSource
import com.example.poster.data.repository.AuthRepositoryImpl
import com.example.poster.data.repository.ChatRepositoryImpl
import com.example.poster.data.repository.MessageRepositoryImpl
import com.example.poster.data.repository.ProfileRepositoryImpl
import com.example.poster.data.repository.SettingsRepositoryImpl
import com.example.poster.domain.repository.AuthRepository
import com.example.poster.domain.repository.ChatRepository
import com.example.poster.domain.repository.MessageRepository
import com.example.poster.domain.repository.ProfileRepository
import com.example.poster.domain.repository.SettingsRepository
import com.example.poster.domain.usecase.ObserveChatsUseCase
import com.example.poster.domain.usecase.app.GetStartDestinationUseCase
import com.example.poster.domain.usecase.auth.CheckAuthStateUseCase
import com.example.poster.domain.usecase.auth.LogoutUseCase
import com.example.poster.domain.usecase.auth.SignInUseCase
import com.example.poster.domain.usecase.auth.SignUpUseCase
import com.example.poster.domain.usecase.auth.VerifyOtpUseCase
import com.example.poster.domain.usecase.chat.GetChatsUseCase
import com.example.poster.domain.usecase.chat.SearchChatsUseCase
import com.example.poster.domain.usecase.message.GetMessagesUseCase
import com.example.poster.domain.usecase.message.ObserveMessagesUseCase
import com.example.poster.domain.usecase.message.SendMessageUseCase
import com.example.poster.domain.usecase.message.SendMessageWithAttachmentsUseCase
import com.example.poster.domain.usecase.profile.GetMyProfileUseCase
import com.example.poster.domain.usecase.profile.GetUserProfileUseCase
import com.example.poster.domain.usecase.profile.UpdateAvatarUseCase
import com.example.poster.domain.usecase.profile.UpdateBioUseCase
import com.example.poster.domain.usecase.profile.UpdateNameUseCase
import com.example.poster.domain.usecase.profile.UpdateUsernameUseCase
import com.example.poster.domain.usecase.settings.CheckMailAccessTokenUseCase
import com.example.poster.domain.usecase.settings.GetSettingsUseCase
import com.example.poster.domain.usecase.settings.SaveMailAccessTokenUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideLocalDataSource(): PosterLocalDataSource = InMemoryPosterLocalDataSource()

    @Provides
    @Singleton
    fun provideRemoteDataSource(): PosterRemoteDataSource = HttpPosterRemoteDataSource()

    @Provides
    @Singleton
    fun provideAuthRepository(
        remoteDataSource: PosterRemoteDataSource,
        localDataSource: PosterLocalDataSource,
    ): AuthRepository = AuthRepositoryImpl(remoteDataSource, localDataSource)

    @Provides
    @Singleton
    fun provideChatRepository(
        remoteDataSource: PosterRemoteDataSource,
        localDataSource: PosterLocalDataSource,
    ): ChatRepository = ChatRepositoryImpl(remoteDataSource, localDataSource)

    @Provides
    @Singleton
    fun provideMessageRepository(
        remoteDataSource: PosterRemoteDataSource,
        localDataSource: PosterLocalDataSource,
        @ApplicationContext context: Context,
    ): MessageRepository = MessageRepositoryImpl(remoteDataSource, localDataSource, context)

    @Provides
    @Singleton
    fun provideProfileRepository(
        remoteDataSource: PosterRemoteDataSource,
        localDataSource: PosterLocalDataSource,
        @ApplicationContext context: Context,
    ): ProfileRepository = ProfileRepositoryImpl(remoteDataSource, localDataSource, context)

    @Provides
    @Singleton
    fun provideSettingsRepository(
        remoteDataSource: PosterRemoteDataSource,
        localDataSource: PosterLocalDataSource,
    ): SettingsRepository = SettingsRepositoryImpl(remoteDataSource, localDataSource)

    @Provides
    fun provideObserveChatsUseCase(repository: ChatRepository): ObserveChatsUseCase =
        ObserveChatsUseCase(repository)

    @Provides
    fun provideSignInUseCase(repository: AuthRepository): SignInUseCase = SignInUseCase(repository)

    @Provides
    fun provideSignUpUseCase(repository: AuthRepository): SignUpUseCase = SignUpUseCase(repository)

    @Provides
    fun provideCheckAuthStateUseCase(repository: AuthRepository): CheckAuthStateUseCase =
        CheckAuthStateUseCase(repository)

    @Provides
    fun provideLogoutUseCase(repository: AuthRepository): LogoutUseCase = LogoutUseCase(repository)

    @Provides
    fun provideVerifyOtpUseCase(repository: AuthRepository): VerifyOtpUseCase =
        VerifyOtpUseCase(repository)

    @Provides
    fun provideGetStartDestinationUseCase(repository: AuthRepository): GetStartDestinationUseCase =
        GetStartDestinationUseCase(repository)

    @Provides
    fun provideGetChatsUseCase(repository: ChatRepository): GetChatsUseCase = GetChatsUseCase(repository)

    @Provides
    fun provideSearchChatsUseCase(repository: ChatRepository): SearchChatsUseCase =
        SearchChatsUseCase(repository)

    @Provides
    fun provideGetMessagesUseCase(repository: MessageRepository): GetMessagesUseCase =
        GetMessagesUseCase(repository)

    @Provides
    fun provideSendMessageUseCase(repository: MessageRepository): SendMessageUseCase =
        SendMessageUseCase(repository)

    @Provides
    fun provideSendMessageWithAttachmentsUseCase(repository: MessageRepository): SendMessageWithAttachmentsUseCase =
        SendMessageWithAttachmentsUseCase(repository)

    @Provides
    fun provideObserveMessagesUseCase(repository: MessageRepository): ObserveMessagesUseCase =
        ObserveMessagesUseCase(repository)

    @Provides
    fun provideGetMyProfileUseCase(repository: ProfileRepository): GetMyProfileUseCase =
        GetMyProfileUseCase(repository)

    @Provides
    fun provideGetUserProfileUseCase(repository: ProfileRepository): GetUserProfileUseCase =
        GetUserProfileUseCase(repository)

    @Provides
    fun provideUpdateUsernameUseCase(repository: ProfileRepository): UpdateUsernameUseCase =
        UpdateUsernameUseCase(repository)

    @Provides
    fun provideUpdateNameUseCase(repository: ProfileRepository): UpdateNameUseCase =
        UpdateNameUseCase(repository)

    @Provides
    fun provideUpdateAvatarUseCase(repository: ProfileRepository): UpdateAvatarUseCase =
        UpdateAvatarUseCase(repository)

    @Provides
    fun provideUpdateBioUseCase(repository: ProfileRepository): UpdateBioUseCase =
        UpdateBioUseCase(repository)

    @Provides
    fun provideGetSettingsUseCase(repository: SettingsRepository): GetSettingsUseCase =
        GetSettingsUseCase(repository)

    @Provides
    fun provideSaveMailAccessTokenUseCase(repository: SettingsRepository): SaveMailAccessTokenUseCase =
        SaveMailAccessTokenUseCase(repository)

    @Provides
    fun provideCheckMailAccessTokenUseCase(repository: SettingsRepository): CheckMailAccessTokenUseCase =
        CheckMailAccessTokenUseCase(repository)
}
