package com.example.poster.domain.usecase.auth

import com.example.poster.domain.repository.AuthRepository

class SignUpUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(name: String, email: String, password: String): Result<Unit> {
        if (name.isBlank()) {
            return Result.failure(IllegalArgumentException("Name is empty"))
        }
        if (email.isBlank()) {
            return Result.failure(IllegalArgumentException("Email is empty"))
        }
        if (!email.contains("@")) {
            return Result.failure(IllegalArgumentException("Invalid email"))
        }
        if (password.isBlank()) {
            return Result.failure(IllegalArgumentException("Password is empty"))
        }

        return authRepository.signUp(name, email, password)
    }
}
