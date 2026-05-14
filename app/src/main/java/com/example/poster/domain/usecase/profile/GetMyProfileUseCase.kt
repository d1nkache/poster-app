package com.example.poster.domain.usecase.profile

import com.example.poster.domain.model.Profile
import com.example.poster.domain.repository.ProfileRepository

class GetMyProfileUseCase(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(): Result<Profile> {
        return profileRepository.getMyProfile()
    }
}
