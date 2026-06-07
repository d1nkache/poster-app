package com.example.poster.domain.model

data class MailAccessSettings(
    val token: String,
    val smtpHost: String? = null,
    val smtpPort: Int? = null,
    val imapHost: String? = null,
    val imapPort: Int? = null,
)
