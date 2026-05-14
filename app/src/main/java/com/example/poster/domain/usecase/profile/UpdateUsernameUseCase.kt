package com.example.poster.domain.usecase.profile

import com.example.poster.domain.model.Profile
import com.example.poster.domain.repository.ProfileRepository

class UpdateUsernameUseCase(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(username: String): Result<Profile> {
        if (username.isBlank()) {
            return Result.failure(IllegalArgumentException("Username is empty"))
        }

        return profileRepository.updateUsername(username)
    }
}
