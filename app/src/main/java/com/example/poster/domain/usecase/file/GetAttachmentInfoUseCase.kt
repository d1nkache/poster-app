package com.example.poster.domain.usecase.file

import com.example.poster.domain.model.Attachment
import com.example.poster.domain.repository.FileRepository

class GetAttachmentInfoUseCase(
    private val fileRepository: FileRepository,
) {
    suspend operator fun invoke(uri: String): Result<Attachment> {
        if (uri.isBlank()) {
            return Result.failure(IllegalArgumentException("Uri is empty"))
        }

        return fileRepository.getAttachmentInfo(uri)
    }
}
