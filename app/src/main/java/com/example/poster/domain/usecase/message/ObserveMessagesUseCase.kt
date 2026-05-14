package com.example.poster.domain.usecase.message

import com.example.poster.domain.model.Message
import com.example.poster.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow

class ObserveMessagesUseCase(
    private val messageRepository: MessageRepository,
) {
    operator fun invoke(chatId: String): Flow<List<Message>> {
        return messageRepository.observeMessages(chatId)
    }
}
