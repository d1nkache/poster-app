package com.example.poster.domain.usecase.auth

import com.example.poster.domain.repository.AuthRepository

class SignInUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        if (email.isBlank()) {
            return Result.failure(IllegalArgumentException("Email is empty"))
        }
        if (!email.contains("@")) {
            return Result.failure(IllegalArgumentException("Invalid email"))
        }
        if (password.isBlank()) {
            return Result.failure(IllegalArgumentException("Password is empty"))
        }

        return authRepository.signIn(email, password)
    }
}
