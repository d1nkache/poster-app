package com.example.poster.domain.usecase.message

import com.example.poster.domain.model.Attachment
import com.example.poster.domain.repository.MessageRepository

class UploadAttachmentUseCase(
    private val messageRepository: MessageRepository,
) {
    suspend operator fun invoke(chatId: String, attachment: Attachment): Result<Attachment> {
        if (chatId.isBlank()) {
            return Result.failure(IllegalArgumentException("Chat id is empty"))
        }

        return messageRepository.uploadAttachment(chatId, attachment)
    }
}
