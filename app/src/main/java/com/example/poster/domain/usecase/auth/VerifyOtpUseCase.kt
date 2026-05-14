package com.example.poster.domain.usecase.auth

import com.example.poster.domain.repository.AuthRepository

class VerifyOtpUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, code: String): Result<Unit> {
        if (email.isBlank()) {
            return Result.failure(IllegalArgumentException("Email is empty"))
        }
        if (code.length != 6 || code.any { !it.isDigit() }) {
            return Result.failure(IllegalArgumentException("Invalid OTP code"))
        }

        return authRepository.verifyOtp(email, code)
    }
}
