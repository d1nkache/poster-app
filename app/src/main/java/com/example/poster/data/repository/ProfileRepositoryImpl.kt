package com.example.poster.data.repository

import com.example.poster.data.mapper.toDomain
import com.example.poster.data.remote.PosterRemoteDataSource
import com.example.poster.domain.model.Profile
import com.example.poster.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val remoteDataSource: PosterRemoteDataSource,
) : ProfileRepository {
    private var myProfile: Profile? = null

    override suspend fun getMyProfile(): Result<Profile> {
        val profile = myProfile ?: remoteDataSource.getProfile("user-1").toDomain()
        myProfile = profile
        return Result.success(profile)
    }

    override suspend fun getUserProfile(userId: String): Result<Profile> {
        return Result.success(remoteDataSource.getProfile(userId).toDomain())
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
        return updateMyProfile { it.copy(avatarUrl = uri) }
    }

    private suspend fun updateMyProfile(transform: (Profile) -> Profile): Result<Profile> {
        val currentProfile = getMyProfile().getOrThrow()
        val updatedProfile = remoteDataSource.updateProfile(transform(currentProfile)).toDomain()
        myProfile = updatedProfile
        return Result.success(updatedProfile)
    }
}
