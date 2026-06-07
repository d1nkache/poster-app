package com.example.poster.data.remote.api

import com.example.poster.data.remote.dto.profile.ProfileDto
import com.example.poster.data.remote.dto.profile.UpdateProfileRequestDto
import com.example.poster.data.remote.dto.profile.UploadAvatarResponseDto

interface ProfileApi {
    suspend fun getMyProfile(): ProfileDto
    suspend fun updateMyProfile(request: UpdateProfileRequestDto): ProfileDto
    suspend fun getProfile(userId: String): ProfileDto
    suspend fun uploadAvatar(avatarPng: ByteArray): UploadAvatarResponseDto
}
