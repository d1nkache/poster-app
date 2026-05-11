package com.example.poster.domain.usecase

import com.example.poster.domain.model.Attachment
import com.example.poster.domain.model.AuthSession
import com.example.poster.domain.model.Chat
import com.example.poster.domain.model.Message
import com.example.poster.domain.model.Profile
import com.example.poster.domain.repository.AuthRepository
import com.example.poster.domain.repository.ChatRepository
import com.example.poster.domain.repository.MessageRepository
import com.example.poster.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class LoginUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(identifier: String, password: String): AuthSession {
        return authRepository.login(identifier, password)
    }
}

class RegisterUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(identifier: String, password: String, displayName: String): AuthSession {
        return authRepository.register(identifier, password, displayName)
    }
}

class VerifyOtpUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(code: String): AuthSession {
        return authRepository.verifyOtp(code)
    }
}

class ObserveChatsUseCase(
    private val chatRepository: ChatRepository,
) {
    operator fun invoke(): Flow<List<Chat>> {
        return chatRepository.observeChats()
    }
}

class ObserveMessagesUseCase(
    private val messageRepository: MessageRepository,
) {
    operator fun invoke(chatId: String): Flow<List<Message>> {
        return messageRepository.observeMessages(chatId)
    }
}

class SendMessageUseCase(
    private val messageRepository: MessageRepository,
) {
    suspend operator fun invoke(chatId: String, text: String, attachments: List<Attachment> = emptyList()) {
        messageRepository.sendMessage(chatId, text, attachments)
    }
}

class GetProfileUseCase(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(userId: String): Profile {
        return profileRepository.getProfile(userId)
    }
}

class UpdateProfileUseCase(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(profile: Profile): Profile {
        return profileRepository.updateProfile(profile)
    }
}
