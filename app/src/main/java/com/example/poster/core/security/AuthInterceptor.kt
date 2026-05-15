package com.example.poster.core.security

interface AuthInterceptor {
    suspend fun authorizationHeader(): String?
}

class BearerAuthInterceptor(
    private val tokenStorage: TokenStorage,
) : AuthInterceptor {
    override suspend fun authorizationHeader(): String? {
        val accessToken = tokenStorage.getAccessToken() ?: return null
        return "Bearer $accessToken"
    }
}
