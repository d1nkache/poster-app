package com.example.poster.data.repository

import com.example.poster.data.mapper.toDomain
import com.example.poster.data.remote.PosterRemoteDataSource
import com.example.poster.domain.model.Profile
import com.example.poster.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val remoteDataSource: PosterRemoteDataSource,
) : ProfileRepository {
    override suspend fun getProfile(userId: String): Profile {
        return remoteDataSource.getProfile(userId).toDomain()
    }

    override suspend fun updateProfile(profile: Profile): Profile {
        return remoteDataSource.updateProfile(profile).toDomain()
    }
}
