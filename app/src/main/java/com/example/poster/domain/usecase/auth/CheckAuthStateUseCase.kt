package com.example.poster.domain.usecase.auth

import com.example.poster.domain.repository.AuthRepository

class CheckAuthStateUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Boolean {
        return authRepository.isAuthorized()
    }
}
