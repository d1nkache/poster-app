package com.example.poster.domain.usecase.chat

import com.example.poster.domain.model.Chat
import com.example.poster.domain.repository.ChatRepository

class SearchChatsUseCase(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(query: String): Result<List<Chat>> {
        return chatRepository.searchChats(query)
    }
}
