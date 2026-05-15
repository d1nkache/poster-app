package com.example.poster.core.security

import com.example.poster.core.storage.StorageKeys

interface MailAccessTokenStorage {
    suspend fun hasToken(): Boolean
    suspend fun saveToken(token: String)
    suspend fun deleteToken()
}

class SecureMailAccessTokenStorage(
    private val secureStorage: SecureStorage,
) : MailAccessTokenStorage {
    override suspend fun hasToken(): Boolean {
        return secureStorage.getString(StorageKeys.MAIL_ACCESS_TOKEN) != null
    }

    override suspend fun saveToken(token: String) {
        secureStorage.putString(StorageKeys.MAIL_ACCESS_TOKEN, token)
    }

    override suspend fun deleteToken() {
        secureStorage.remove(StorageKeys.MAIL_ACCESS_TOKEN)
    }
}
