package com.example.poster.core.security

import com.example.poster.core.storage.StorageKeys

interface TokenStorage {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun clearTokens()
}

class SecureTokenStorage(
    private val secureStorage: SecureStorage,
) : TokenStorage {
    override suspend fun getAccessToken(): String? {
        return secureStorage.getString(StorageKeys.ACCESS_TOKEN)
    }

    override suspend fun getRefreshToken(): String? {
        return secureStorage.getString(StorageKeys.REFRESH_TOKEN)
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        secureStorage.putString(StorageKeys.ACCESS_TOKEN, accessToken)
        secureStorage.putString(StorageKeys.REFRESH_TOKEN, refreshToken)
    }

    override suspend fun clearTokens() {
        secureStorage.remove(StorageKeys.ACCESS_TOKEN)
        secureStorage.remove(StorageKeys.REFRESH_TOKEN)
    }
}
