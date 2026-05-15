package com.example.poster.data.repository

import com.example.poster.domain.model.Chat
import com.example.poster.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockChatRepository : ChatRepository {
    private val chatsFlow = MutableStateFlow(mockChats())

    override suspend fun getChats(): Result<List<Chat>> {
        return Result.success(chatsFlow.value)
    }

    override suspend fun searchChats(query: String): Result<List<Chat>> {
        val normalizedQuery = query.trim()
        val chats = if (normalizedQuery.isBlank()) {
            chatsFlow.value
        } else {
            chatsFlow.value.filter {
                it.title.contains(normalizedQuery, ignoreCase = true) ||
                    it.lastMessage.contains(normalizedQuery, ignoreCase = true)
            }
        }
        return Result.success(chats)
    }

    override suspend fun syncChats(): Result<Unit> {
        chatsFlow.value = mockChats()
        return Result.success(Unit)
    }

    override fun observeChats(): Flow<List<Chat>> {
        return chatsFlow.asStateFlow()
    }

    override suspend fun markChatAsRead(chatId: String): Result<Unit> {
        chatsFlow.value = chatsFlow.value.map { chat ->
            if (chat.id == chatId) chat.copy(unreadCount = 0) else chat
        }
        return Result.success(Unit)
    }

    override suspend fun deleteChat(chatId: String): Result<Unit> {
        chatsFlow.value = chatsFlow.value.filterNot { it.id == chatId }
        return Result.success(Unit)
    }

    private fun mockChats(): List<Chat> {
        return listOf(
            Chat(
                id = "1",
                title = "Alice Johnson",
                initials = "AJ",
                lastMessage = "See you tomorrow!",
                lastMessageTime = "10:30 AM",
                unreadCount = 2,
            ),
            Chat(
                id = "2",
                title = "Bob Smith",
                initials = "BS",
                lastMessage = "Thanks for the update",
                lastMessageTime = "Yesterday",
                unreadCount = 0,
            ),
            Chat(
                id = "3",
                title = "Carol White",
                initials = "CW",
                lastMessage = "Meeting at 3 PM?",
                lastMessageTime = "Yesterday",
                unreadCount = 1,
            ),
            Chat(
                id = "4",
                title = "David Brown",
                initials = "DB",
                lastMessage = "Perfect, sounds good!",
                lastMessageTime = "2 days ago",
                unreadCount = 0,
            ),
            Chat(
                id = "5",
                title = "Emma Davis",
                initials = "ED",
                lastMessage = "I'll send the files",
                lastMessageTime = "3 days ago",
                unreadCount = 0,
            ),
            Chat(
                id = "6",
                title = "Frank King",
                initials = "FK",
                lastMessage = "SMTP sync completed",
                lastMessageTime = "Monday",
                unreadCount = 0,
            ),
            Chat(
                id = "7",
                title = "Grace Lee",
                initials = "GL",
                lastMessage = "Can we review the IMAP logs?",
                lastMessageTime = "Sunday",
                unreadCount = 4,
            ),
            Chat(
                id = "8",
                title = "Henry Miller",
                initials = "HM",
                lastMessage = "Token settings look correct",
                lastMessageTime = "Saturday",
                unreadCount = 0,
            ),
            Chat(
                id = "9",
                title = "Ivy Nelson",
                initials = "IN",
                lastMessage = "New message arrived via mail",
                lastMessageTime = "Friday",
                unreadCount = 3,
            ),
            Chat(
                id = "10",
                title = "Jack Owens",
                initials = "JO",
                lastMessage = "I'll check the server response",
                lastMessageTime = "Friday",
                unreadCount = 0,
            ),
            Chat(
                id = "11",
                title = "Kate Parker",
                initials = "KP",
                lastMessage = "Thanks, it works now",
                lastMessageTime = "Thursday",
                unreadCount = 0,
            ),
            Chat(
                id = "12",
                title = "Liam Reed",
                initials = "LR",
                lastMessage = "Let's deploy the Ktor endpoint",
                lastMessageTime = "Wednesday",
                unreadCount = 1,
            ),
        )
    }
}
