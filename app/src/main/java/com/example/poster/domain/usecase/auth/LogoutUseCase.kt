package com.example.poster.domain.usecase.auth

import com.example.poster.domain.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.logout()
    }
}
