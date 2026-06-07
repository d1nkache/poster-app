package com.example.poster.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.poster.data.mapper.toDomain
import com.example.poster.data.mapper.toLocal
import com.example.poster.data.remote.PosterApiException
import com.example.poster.data.remote.PosterRemoteDataSource
import com.example.poster.domain.model.Profile
import com.example.poster.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val remoteDataSource: PosterRemoteDataSource,
    private val localDataSource: com.example.poster.data.local.PosterLocalDataSource,
    private val context: Context? = null,
) : ProfileRepository {
    private var myProfile: Profile? = null

    override suspend fun getMyProfile(): Result<Profile> {
        Log.d(TAG, "getMyProfile requested")
        return runCatching {
            val profile = myProfile ?: authorized { remoteDataSource.getMyProfile(it) }.toDomain()
            myProfile = profile
            Log.d(TAG, "getMyProfile succeeded: id=${profile.id}")
            profile
        }.onFailure { error ->
            Log.e(TAG, "getMyProfile failed", error)
        }
    }

    override suspend fun getUserProfile(userId: String): Result<Profile> {
        Log.d(TAG, "getUserProfile requested: userId=$userId")
        return runCatching {
            authorized { remoteDataSource.getProfile(it, userId) }.toDomain()
        }.onFailure { error ->
            Log.e(TAG, "getUserProfile failed: userId=$userId", error)
        }
    }

    override suspend fun updateName(name: String): Result<Profile> {
        return updateMyProfile { it.copy(name = name) }
    }

    override suspend fun updateUsername(username: String): Result<Profile> {
        return updateMyProfile { it.copy(username = username) }
    }

    override suspend fun updateBio(bio: String): Result<Profile> {
        return updateMyProfile { it.copy(bio = bio) }
    }

    override suspend fun updateAvatar(uri: String): Result<Profile> {
        Log.d(TAG, "updateAvatar requested")
        return runCatching {
            val appContext = context
                ?: throw IllegalStateException("Application context is required to upload avatar")
            val avatarUri = Uri.parse(uri)
            val contentType = appContext.contentResolver.getType(avatarUri)
            if (contentType != "image/png") {
                throw IllegalArgumentException("Avatar must be PNG")
            }
            val bytes = appContext.contentResolver.openInputStream(avatarUri)?.use { it.readBytes() }
                ?: throw IllegalArgumentException("Cannot read avatar file")
            val avatarUrl = authorized { remoteDataSource.uploadAvatar(it, bytes) }
            val currentProfile = getMyProfile().getOrThrow()
            val updatedProfile = currentProfile.copy(avatarUrl = avatarUrl)
            myProfile = updatedProfile
            Log.d(TAG, "updateAvatar succeeded: id=${updatedProfile.id}")
            updatedProfile
        }.onFailure { error ->
            Log.e(TAG, "updateAvatar failed", error)
        }
    }

    private suspend fun updateMyProfile(transform: (Profile) -> Profile): Result<Profile> = runCatching {
        val currentProfile = getMyProfile().getOrThrow()
        val desiredProfile = transform(currentProfile)
        Log.d(TAG, "updateMyProfile requested: id=${currentProfile.id}")
        val updatedProfile = authorized { token -> remoteDataSource.updateProfile(
            accessToken = token,
            name = desiredProfile.name.takeIf { it != currentProfile.name },
            username = desiredProfile.username.takeIf { it != currentProfile.username },
            bio = desiredProfile.bio.takeIf { it != currentProfile.bio },
        ) }.toDomain()
        myProfile = updatedProfile
        Log.d(TAG, "updateMyProfile succeeded: id=${updatedProfile.id}")
        updatedProfile
    }.onFailure { error ->
        Log.e(TAG, "updateMyProfile failed", error)
    }

    private suspend fun accessToken(): String? {
        return localDataSource.getAuthSession()?.accessToken
    }

    private suspend fun <T> authorized(block: suspend (String?) -> T): T {
        return try {
            block(accessToken())
        } catch (error: PosterApiException) {
            if (error.statusCode != 401) throw error
            Log.d(TAG, "Access token expired, refreshing for profile request")
            val currentSession = localDataSource.getAuthSession() ?: throw error
            val refreshedSession = remoteDataSource.refresh(currentSession.refreshToken).toDomain()
            localDataSource.saveAuthSession(refreshedSession.toLocal())
            block(refreshedSession.accessToken)
        }
    }

    private companion object {
        private const val TAG = "ProfileRepository"
    }
}
