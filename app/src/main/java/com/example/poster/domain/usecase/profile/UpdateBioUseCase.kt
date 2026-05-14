package com.example.poster.domain.usecase.profile

import com.example.poster.domain.model.Profile
import com.example.poster.domain.repository.ProfileRepository

class UpdateBioUseCase(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(bio: String): Result<Profile> {
        return profileRepository.updateBio(bio)
    }
}
