package com.example.poster.domain.usecase.profile

import com.example.poster.domain.model.Profile
import com.example.poster.domain.repository.ProfileRepository

class UpdateNameUseCase(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(name: String): Result<Profile> {
        if (name.isBlank()) {
            return Result.failure(IllegalArgumentException("Name is empty"))
        }

        return profileRepository.updateName(name.trim())
    }
}
