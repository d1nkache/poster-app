package com.example.poster.data.repository

import com.example.poster.data.mapper.toDomain
import com.example.poster.data.remote.PosterRemoteDataSource
import com.example.poster.domain.model.Chat
import com.example.poster.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatRepositoryImpl(
    private val remoteDataSource: PosterRemoteDataSource,
) : ChatRepository {
    private val chatsFlow = MutableStateFlow<List<Chat>>(emptyList())

    override fun observeChats(): Flow<List<Chat>> {
        return chatsFlow.asStateFlow()
    }

    override suspend fun refreshChats() {
        chatsFlow.value = remoteDataSource.getChats().map { it.toDomain() }
    }
}
