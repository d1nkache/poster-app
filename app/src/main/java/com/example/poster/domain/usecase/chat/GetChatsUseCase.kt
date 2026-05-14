package com.example.poster.domain.usecase.chat

import com.example.poster.domain.model.Chat
import com.example.poster.domain.repository.ChatRepository

class GetChatsUseCase(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(): Result<List<Chat>> {
        return chatRepository.getChats()
    }
}
