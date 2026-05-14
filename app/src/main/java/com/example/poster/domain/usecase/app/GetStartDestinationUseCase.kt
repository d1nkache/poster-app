package com.example.poster.domain.usecase.app

import com.example.poster.domain.model.StartDestination
import com.example.poster.domain.repository.AuthRepository

class GetStartDestinationUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): StartDestination {
        return if (authRepository.isAuthorized()) {
            StartDestination.CHATS
        } else {
            StartDestination.AUTH
        }
    }
}
