package com.example.poster.core.security

interface SecureStorage {
    suspend fun getString(key: String): String?
    suspend fun putString(key: String, value: String)
    suspend fun remove(key: String)
    suspend fun clear()
}

class InMemorySecureStorage : SecureStorage {
    private val values = mutableMapOf<String, String>()

    override suspend fun getString(key: String): String? {
        return values[key]
    }

    override suspend fun putString(key: String, value: String) {
        values[key] = value
    }

    override suspend fun remove(key: String) {
        values.remove(key)
    }

    override suspend fun clear() {
        values.clear()
    }
}
