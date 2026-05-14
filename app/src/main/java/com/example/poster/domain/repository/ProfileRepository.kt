package com.example.poster.domain.repository

import com.example.poster.domain.model.Profile

interface ProfileRepository {
    suspend fun getMyProfile(): Result<Profile>
    suspend fun getUserProfile(userId: String): Result<Profile>
    suspend fun updateName(name: String): Result<Profile>
    suspend fun updateUsername(username: String): Result<Profile>
    suspend fun updateBio(bio: String): Result<Profile>
    suspend fun updateAvatar(uri: String): Result<Profile>
}
