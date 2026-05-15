package com.example.poster.domain.usecase.file

import com.example.poster.domain.model.Attachment
import com.example.poster.domain.repository.FileRepository

class OpenAttachmentUseCase(
    private val fileRepository: FileRepository,
) {
    suspend operator fun invoke(attachment: Attachment): Result<Unit> {
        return fileRepository.openAttachment(attachment)
    }
}
