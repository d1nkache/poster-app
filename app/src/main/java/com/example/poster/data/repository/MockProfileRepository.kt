package com.example.poster.data.repository

import com.example.poster.domain.model.Profile
import com.example.poster.domain.repository.ProfileRepository

class MockProfileRepository : ProfileRepository {
    private var myProfile = Profile(
        id = "user-1",
        name = "Your Name",
        username = "@your.username",
        email = "your.email@example.com",
        bio = "Hey there! I'm using Poster.",
        avatarUrl = null,
        isOnline = true,
    )

    override suspend fun getMyProfile(): Result<Profile> {
        return Result.success(myProfile)
    }

    override suspend fun getUserProfile(userId: String): Result<Profile> {
        return Result.success(
            Profile(
                id = userId,
                name = "Alice Johnson",
                username = "@alice.johnson",
                email = "alice@example.com",
                bio = "Product designer passionate about creating beautiful and functional user experiences.",
                avatarUrl = null,
                isOnline = true,
            )
        )
    }

    override suspend fun updateName(name: String): Result<Profile> {
        myProfile = myProfile.copy(name = name)
        return Result.success(myProfile)
    }

    override suspend fun updateUsername(username: String): Result<Profile> {
        myProfile = myProfile.copy(username = username)
        return Result.success(myProfile)
    }

    override suspend fun updateBio(bio: String): Result<Profile> {
        myProfile = myProfile.copy(bio = bio)
        return Result.success(myProfile)
    }

    override suspend fun updateAvatar(uri: String): Result<Profile> {
        myProfile = myProfile.copy(avatarUrl = uri)
        return Result.success(myProfile)
    }
}
