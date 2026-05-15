package com.example.poster.domain.usecase.profile

import com.example.poster.domain.model.Profile
import com.example.poster.domain.repository.ProfileRepository

class UpdateAvatarUseCase(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(uri: String): Result<Profile> {
        if (uri.isBlank()) {
            return Result.failure(IllegalArgumentException("Avatar uri is empty"))
        }

        return profileRepository.updateAvatar(uri)
    }
}
