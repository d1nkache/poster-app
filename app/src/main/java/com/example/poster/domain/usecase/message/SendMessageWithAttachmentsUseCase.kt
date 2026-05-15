package com.example.poster.domain.usecase.message

import com.example.poster.domain.model.Attachment
import com.example.poster.domain.model.Message
import com.example.poster.domain.repository.MessageRepository

class SendMessageWithAttachmentsUseCase(
    private val messageRepository: MessageRepository,
) {
    suspend operator fun invoke(
        chatId: String,
        text: String,
        attachments: List<Attachment>,
    ): Result<Message> {
        if (chatId.isBlank()) {
            return Result.failure(IllegalArgumentException("Chat id is empty"))
        }
        if (text.isBlank() && attachments.isEmpty()) {
            return Result.failure(IllegalArgumentException("Message cannot be empty"))
        }

        return messageRepository.sendMessage(
            chatId = chatId,
            text = text.trim(),
            attachments = attachments,
        )
    }
}
