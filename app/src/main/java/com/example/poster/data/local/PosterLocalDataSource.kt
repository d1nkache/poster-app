package com.example.poster.data.local

data class LocalAuthSessionEntity(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
)

interface PosterLocalDataSource {
    suspend fun getAuthSession(): LocalAuthSessionEntity?
    suspend fun saveAuthSession(session: LocalAuthSessionEntity)
    suspend fun clearAuthSession()
}

class InMemoryPosterLocalDataSource : PosterLocalDataSource {
    private var session: LocalAuthSessionEntity? = null

    override suspend fun getAuthSession(): LocalAuthSessionEntity? {
        return session
    }

    override suspend fun saveAuthSession(session: LocalAuthSessionEntity) {
        this.session = session
    }

    override suspend fun clearAuthSession() {
        session = null
    }
}
