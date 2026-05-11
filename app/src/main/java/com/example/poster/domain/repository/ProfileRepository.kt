package com.example.poster.domain.repository

import com.example.poster.domain.model.Profile

interface ProfileRepository {
    suspend fun getProfile(userId: String): Profile
    suspend fun updateProfile(profile: Profile): Profile
}
