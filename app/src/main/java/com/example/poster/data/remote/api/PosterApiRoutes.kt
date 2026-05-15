package com.example.poster.data.remote.api

object PosterApiRoutes {
    const val SIGN_UP = "/auth/sign-up"
    const val SIGN_IN = "/auth/sign-in"
    const val VERIFY_OTP = "/auth/verify-otp"
    const val RESEND_OTP = "/auth/resend-otp"
    const val REFRESH = "/auth/refresh"
    const val LOGOUT = "/auth/logout"
    const val AUTH_ME = "/auth/me"

    const val PROFILE_ME = "/profile/me"
    const val PROFILE_AVATAR = "/profile/avatar"

    const val SETTINGS = "/settings"
    const val MAIL_ACCESS_TOKEN = "/settings/mail-access-token"
    const val MAIL_ACCESS_TOKEN_STATUS = "/settings/mail-access-token/status"

    const val CHATS = "/chats"
    const val CHATS_SYNC = "/chats/sync"

    const val WEB_SOCKET = "/ws"

    fun profile(userId: String): String = "/profile/$userId"
    fun chat(chatId: String): String = "/chats/$chatId"
    fun chatRead(chatId: String): String = "/chats/$chatId/read"
    fun chatMessages(chatId: String): String = "/chats/$chatId/messages"
    fun message(messageId: String): String = "/messages/$messageId"
    fun messageRead(messageId: String): String = "/messages/$messageId/read"
    fun messageRetry(messageId: String): String = "/messages/$messageId/retry"
}
