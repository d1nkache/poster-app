package com.example.poster.domain.usecase.message

import com.example.poster.domain.model.Message
import com.example.poster.domain.repository.MessageRepository

class GetMessagesUseCase(
    private val messageRepository: MessageRepository,
) {
    suspend operator fun invoke(chatId: String): Result<List<Message>> {
        if (chatId.isBlank()) {
            return Result.failure(IllegalArgumentException("Chat id is empty"))
        }

        return messageRepository.getMessages(chatId)
    }
}
