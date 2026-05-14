package com.example.poster.domain.usecase.message

import com.example.poster.domain.model.Message
import com.example.poster.domain.repository.MessageRepository

class SendMessageUseCase(
    private val messageRepository: MessageRepository,
) {
    suspend operator fun invoke(chatId: String, text: String): Result<Message> {
        if (chatId.isBlank()) {
            return Result.failure(IllegalArgumentException("Chat id is empty"))
        }
        if (text.isBlank()) {
            return Result.failure(IllegalArgumentException("Message is empty"))
        }

        return messageRepository.sendMessage(chatId, text.trim())
    }
}
