package com.example.poster.domain.usecase.profile

import com.example.poster.domain.model.Profile
import com.example.poster.domain.repository.ProfileRepository

class GetUserProfileUseCase(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(userId: String): Result<Profile> {
        if (userId.isBlank()) {
            return Result.failure(IllegalArgumentException("User id is empty"))
        }

        return profileRepository.getUserProfile(userId)
    }
}
