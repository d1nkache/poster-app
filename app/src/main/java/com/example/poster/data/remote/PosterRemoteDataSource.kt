package com.example.poster.data.remote

import android.util.Log
import com.example.poster.core.network.NetworkConfig
import com.example.poster.data.remote.dto.chat.ChatDto
import com.example.poster.data.remote.dto.message.MessageAttachmentDto
import com.example.poster.data.remote.dto.message.MessageDto
import com.example.poster.data.remote.dto.profile.ProfileDto
import com.example.poster.data.remote.dto.settings.SettingsDto
import com.example.poster.domain.model.MailAccessSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

data class RemoteAuthSessionDto(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
)

data class RemoteAttachmentDto(
    val id: String,
    val type: String,
    val localUri: String?,
    val remoteUrl: String?,
    val fileName: String,
    val mimeType: String,
    val sizeBytes: Long,
    val uploadStatus: String,
)

data class RemoteOutgoingAttachmentDto(
    val fileName: String,
    val contentType: String,
    val bytes: ByteArray,
)

interface PosterRemoteDataSource {
    suspend fun login(email: String, password: String): RemoteAuthSessionDto
    suspend fun register(email: String, password: String, displayName: String)
    suspend fun verifyOtp(email: String, code: String): RemoteAuthSessionDto
    suspend fun refresh(refreshToken: String): RemoteAuthSessionDto
    suspend fun logout(accessToken: String?)
    suspend fun getChats(accessToken: String?): List<ChatDto>
    suspend fun getMessages(accessToken: String?, chatId: String): List<MessageDto>
    suspend fun sendMessage(accessToken: String?, chatId: String, text: String): MessageDto
    suspend fun sendMessageWithAttachments(
        accessToken: String?,
        chatId: String,
        text: String,
        attachments: List<RemoteOutgoingAttachmentDto>,
    ): MessageDto
    suspend fun markChatAsRead(accessToken: String?, chatId: String)
    suspend fun markMessageAsRead(accessToken: String?, messageId: String)
    suspend fun deleteMessage(accessToken: String?, messageId: String)
    suspend fun getMyProfile(accessToken: String?): ProfileDto
    suspend fun getProfile(accessToken: String?, userId: String): ProfileDto
    suspend fun updateProfile(accessToken: String?, name: String? = null, username: String? = null, bio: String? = null): ProfileDto
    suspend fun uploadAvatar(accessToken: String?, pngBytes: ByteArray): String
    suspend fun getSettings(accessToken: String?): SettingsDto
    suspend fun changeLanguage(accessToken: String?, language: String): SettingsDto
    suspend fun saveMailAccessToken(accessToken: String?, settings: MailAccessSettings)
    suspend fun deleteMailAccessToken(accessToken: String?)
    suspend fun hasMailAccessToken(accessToken: String?): Boolean
}

class HttpPosterRemoteDataSource(
    private val baseUrl: String = NetworkConfig.BASE_URL,
) : PosterRemoteDataSource {
    private companion object {
        const val TAG = "PosterApi"
        const val REDACTED = "***"
    }

    override suspend fun login(email: String, password: String): RemoteAuthSessionDto {
        return authSession(
            request(
                method = "POST",
                path = "auth/login",
                body = jsonObject("email" to email, "password" to password),
            )
        )
    }

    override suspend fun register(email: String, password: String, displayName: String) {
        request(
            method = "POST",
            path = "auth/register",
            body = jsonObject("email" to email, "password" to password, "displayName" to displayName),
            expectedCodes = setOf(HttpURLConnection.HTTP_CREATED),
        )
    }

    override suspend fun verifyOtp(email: String, code: String): RemoteAuthSessionDto {
        return authSession(
            request(
                method = "POST",
                path = "auth/verify-otp",
                body = jsonObject("email" to email, "code" to code),
            )
        )
    }

    override suspend fun refresh(refreshToken: String): RemoteAuthSessionDto {
        return authSession(
            request(
                method = "POST",
                path = "auth/refresh",
                body = jsonObject("refreshToken" to refreshToken),
            )
        )
    }

    override suspend fun logout(accessToken: String?) {
        request(
            method = "POST",
            path = "auth/logout",
            accessToken = accessToken,
            expectedCodes = setOf(HttpURLConnection.HTTP_NO_CONTENT),
        )
    }

    override suspend fun getChats(accessToken: String?): List<ChatDto> {
        val contacts = request(method = "GET", path = "contacts", accessToken = accessToken)
            .getJSONArray("items")

        return buildList {
            for (index in 0 until contacts.length()) {
                val contact = contacts.getJSONObject(index)
                val contactId = contact.getLong("id")
                add(
                    request(
                        method = "POST",
                        path = "contacts/$contactId/chat",
                        accessToken = accessToken,
                    ).toChatDto()
                )
            }
        }
    }

    override suspend fun getMessages(accessToken: String?, chatId: String): List<MessageDto> {
        val items = request(
            method = "GET",
            path = "chats/$chatId/messages?limit=50&offset=0",
            accessToken = accessToken,
        ).getJSONArray("items")

        return buildList {
            for (index in 0 until items.length()) {
                add(items.getJSONObject(index).toMessageDto())
            }
        }
    }

    override suspend fun sendMessage(accessToken: String?, chatId: String, text: String): MessageDto {
        return request(
            method = "POST",
            path = "chats/$chatId/messages",
            accessToken = accessToken,
            body = jsonObject("bodyText" to text),
            expectedCodes = setOf(HttpURLConnection.HTTP_CREATED),
        ).messagePayload().toMessageDto()
    }

    override suspend fun sendMessageWithAttachments(
        accessToken: String?,
        chatId: String,
        text: String,
        attachments: List<RemoteOutgoingAttachmentDto>,
    ): MessageDto {
        return multipartMessageRequest(
            path = "chats/$chatId/messages",
            accessToken = accessToken,
            bodyText = text,
            attachments = attachments,
        ).messagePayload().toMessageDto()
    }

    override suspend fun markChatAsRead(accessToken: String?, chatId: String) {
        request(
            method = "PATCH",
            path = "chats/$chatId/messages/read",
            accessToken = accessToken,
            expectedCodes = setOf(HttpURLConnection.HTTP_NO_CONTENT),
        )
    }

    override suspend fun markMessageAsRead(accessToken: String?, messageId: String) {
        request(
            method = "PATCH",
            path = "messages/$messageId/read",
            accessToken = accessToken,
            expectedCodes = setOf(HttpURLConnection.HTTP_NO_CONTENT),
        )
    }

    override suspend fun deleteMessage(accessToken: String?, messageId: String) {
        request(
            method = "DELETE",
            path = "messages/$messageId",
            accessToken = accessToken,
            expectedCodes = setOf(HttpURLConnection.HTTP_NO_CONTENT),
        )
    }

    override suspend fun getMyProfile(accessToken: String?): ProfileDto {
        return request(method = "GET", path = "profile/me", accessToken = accessToken).toProfileDto()
    }

    override suspend fun getProfile(accessToken: String?, userId: String): ProfileDto {
        return request(method = "GET", path = "profile/$userId", accessToken = accessToken).toProfileDto()
    }

    override suspend fun updateProfile(
        accessToken: String?,
        name: String?,
        username: String?,
        bio: String?,
    ): ProfileDto {
        val body = JSONObject().apply {
            name?.let { put("name", it) }
            username?.let { put("username", it) }
            bio?.let { put("bio", it) }
        }
        return request(method = "PATCH", path = "profile/me", accessToken = accessToken, body = body)
            .toProfileDto()
    }

    override suspend fun uploadAvatar(accessToken: String?, pngBytes: ByteArray): String = withContext(Dispatchers.IO) {
        val boundary = "PosterBoundary-${UUID.randomUUID()}"
        val requestUrl = URL(baseUrl.trimEnd('/') + "/profile/avatar")
        val connection = requestUrl.openConnection() as HttpURLConnection
        val startedAt = System.currentTimeMillis()
        var responseReceived = false

        Log.d(
            TAG,
            "Request: POST $requestUrl auth=${accessToken.hasBearerToken()} body=multipart/avatar size=${pngBytes.size}",
        )

        try {
            connection.requestMethod = "POST"
            connection.connectTimeout = NetworkConfig.CONNECT_TIMEOUT_SECONDS.toInt() * 1000
            connection.readTimeout = NetworkConfig.READ_TIMEOUT_SECONDS.toInt() * 1000
            connection.doOutput = true
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
            accessToken?.takeIf(String::isNotBlank)?.let {
                connection.setRequestProperty("Authorization", "Bearer $it")
            }

            connection.outputStream.use { output ->
                output.write("--$boundary\r\n".toByteArray())
                output.write(
                    "Content-Disposition: form-data; name=\"avatar\"; filename=\"avatar.png\"\r\n"
                        .toByteArray()
                )
                output.write("Content-Type: image/png\r\n\r\n".toByteArray())
                output.write(pngBytes)
                output.write("\r\n--$boundary--\r\n".toByteArray())
            }

            val responseCode = connection.responseCode
            val responseText = connection.inputOrErrorText()
            responseReceived = true
            val durationMs = System.currentTimeMillis() - startedAt
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.e(
                    TAG,
                    "Response: POST $requestUrl -> $responseCode in ${durationMs}ms error=${responseText.safePreview()}",
                )
                throw PosterApiException(responseCode, responseText.errorMessage())
            }
            Log.d(
                TAG,
                "Response: POST $requestUrl -> $responseCode in ${durationMs}ms body=${responseText.safePreview()}",
            )
            JSONObject(responseText).getString("avatarUrl").toAbsoluteUrl()
        } catch (throwable: Throwable) {
            if (!responseReceived) {
                val durationMs = System.currentTimeMillis() - startedAt
                Log.e(TAG, "Request failed: POST $requestUrl in ${durationMs}ms", throwable)
            }
            throw throwable
        } finally {
            connection.disconnect()
        }
    }

    override suspend fun getSettings(accessToken: String?): SettingsDto {
        return request(method = "GET", path = "settings", accessToken = accessToken).toSettingsDto()
    }

    override suspend fun changeLanguage(accessToken: String?, language: String): SettingsDto {
        return request(
            method = "PATCH",
            path = "settings",
            accessToken = accessToken,
            body = jsonObject("language" to language),
        ).toSettingsDto()
    }

    override suspend fun saveMailAccessToken(accessToken: String?, settings: MailAccessSettings) {
        request(
            method = "PUT",
            path = "settings/mail-access-token",
            accessToken = accessToken,
            body = JSONObject().apply {
                put("token", settings.token)
                settings.smtpHost?.let { put("smtpHost", it) }
                settings.smtpPort?.let { put("smtpPort", it) }
                settings.imapHost?.let { put("imapHost", it) }
                settings.imapPort?.let { put("imapPort", it) }
            },
            expectedCodes = setOf(HttpURLConnection.HTTP_NO_CONTENT),
        )
    }

    override suspend fun deleteMailAccessToken(accessToken: String?) {
        request(
            method = "DELETE",
            path = "settings/mail-access-token",
            accessToken = accessToken,
            expectedCodes = setOf(HttpURLConnection.HTTP_NO_CONTENT),
        )
    }

    override suspend fun hasMailAccessToken(accessToken: String?): Boolean {
        return request(
            method = "GET",
            path = "settings/mail-access-token/status",
            accessToken = accessToken,
        ).getBoolean("configured")
    }

    private suspend fun multipartMessageRequest(
        path: String,
        accessToken: String?,
        bodyText: String,
        attachments: List<RemoteOutgoingAttachmentDto>,
    ): JSONObject = withContext(Dispatchers.IO) {
        val boundary = "PosterBoundary-${UUID.randomUUID()}"
        val requestUrl = URL(baseUrl.trimEnd('/') + "/" + path.trimStart('/'))
        val connection = requestUrl.openConnection() as HttpURLConnection
        val startedAt = System.currentTimeMillis()
        var responseReceived = false

        Log.d(
            TAG,
            "Request: POST $requestUrl auth=${accessToken.hasBearerToken()} body=multipart/message textLength=${bodyText.length} attachments=${attachments.size}",
        )

        try {
            connection.requestMethod = "POST"
            connection.connectTimeout = NetworkConfig.CONNECT_TIMEOUT_SECONDS.toInt() * 1000
            connection.readTimeout = NetworkConfig.READ_TIMEOUT_SECONDS.toInt() * 1000
            connection.doOutput = true
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
            accessToken?.takeIf(String::isNotBlank)?.let {
                connection.setRequestProperty("Authorization", "Bearer $it")
            }

            connection.outputStream.use { output ->
                output.writeMultipartField(boundary, "bodyText", bodyText)
                attachments.forEach { attachment ->
                    output.writeMultipartFile(boundary, "attachments", attachment)
                }
                output.write("--$boundary--\r\n".toByteArray())
            }

            val responseCode = connection.responseCode
            val responseText = connection.inputOrErrorText()
            responseReceived = true
            val durationMs = System.currentTimeMillis() - startedAt
            if (responseCode !in setOf(HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_CREATED)) {
                Log.e(
                    TAG,
                    "Response: POST $requestUrl -> $responseCode in ${durationMs}ms error=${responseText.safePreview()}",
                )
                throw PosterApiException(responseCode, responseText.errorMessage())
            }
            Log.d(
                TAG,
                "Response: POST $requestUrl -> $responseCode in ${durationMs}ms body=${responseText.safePreview()}",
            )

            if (responseText.isBlank()) {
                JSONObject()
            } else {
                JSONObject(responseText)
            }
        } catch (throwable: Throwable) {
            if (!responseReceived) {
                val durationMs = System.currentTimeMillis() - startedAt
                Log.e(TAG, "Request failed: POST $requestUrl in ${durationMs}ms", throwable)
            }
            throw throwable
        } finally {
            connection.disconnect()
        }
    }

    private suspend fun request(
        method: String,
        path: String,
        accessToken: String? = null,
        body: JSONObject? = null,
        expectedCodes: Set<Int> = setOf(HttpURLConnection.HTTP_OK),
    ): JSONObject = withContext(Dispatchers.IO) {
        val requestUrl = URL(baseUrl.trimEnd('/') + "/" + path.trimStart('/'))
        val connection = requestUrl.openConnection() as HttpURLConnection
        val startedAt = System.currentTimeMillis()
        var responseReceived = false

        Log.d(
            TAG,
            "Request: $method $requestUrl auth=${accessToken.hasBearerToken()} body=${body?.sanitizedForLog() ?: "<empty>"}",
        )

        try {
            connection.requestMethod = method
            connection.connectTimeout = NetworkConfig.CONNECT_TIMEOUT_SECONDS.toInt() * 1000
            connection.readTimeout = NetworkConfig.READ_TIMEOUT_SECONDS.toInt() * 1000
            connection.setRequestProperty("Accept", "application/json")
            accessToken?.takeIf(String::isNotBlank)?.let {
                connection.setRequestProperty("Authorization", "Bearer $it")
            }

            if (body != null) {
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")
                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(body.toString())
                }
            }

            val responseCode = connection.responseCode
            val responseText = connection.inputOrErrorText()
            responseReceived = true
            val durationMs = System.currentTimeMillis() - startedAt
            if (responseCode !in expectedCodes) {
                Log.e(
                    TAG,
                    "Response: $method $requestUrl -> $responseCode in ${durationMs}ms error=${responseText.safePreview()}",
                )
                throw PosterApiException(responseCode, responseText.errorMessage())
            }
            Log.d(
                TAG,
                "Response: $method $requestUrl -> $responseCode in ${durationMs}ms body=${responseText.safePreview()}",
            )

            if (responseText.isBlank()) {
                JSONObject()
            } else {
                JSONObject(responseText)
            }
        } catch (throwable: Throwable) {
            if (!responseReceived) {
                val durationMs = System.currentTimeMillis() - startedAt
                Log.e(TAG, "Request failed: $method $requestUrl in ${durationMs}ms", throwable)
            }
            throw throwable
        } finally {
            connection.disconnect()
        }
    }

    private fun HttpURLConnection.inputOrErrorText(): String {
        val stream = if (responseCode in 200..299) inputStream else errorStream
        return stream?.bufferedReader()?.use { it.readText() }.orEmpty()
    }

    private fun String.errorMessage(): String {
        return runCatching {
            val error = JSONObject(this)
            error.optString("message").ifBlank { error.optString("code") }
        }.getOrDefault(this).ifBlank { "Network request failed" }
    }

    private fun authSession(json: JSONObject): RemoteAuthSessionDto {
        val profile = json.getJSONObject("profile")
        return RemoteAuthSessionDto(
            accessToken = json.getString("accessToken"),
            refreshToken = json.getString("refreshToken"),
            userId = profile.getString("id"),
        )
    }

    private fun JSONObject.messagePayload(): JSONObject {
        return optJSONObject("message") ?: this
    }

    private fun JSONObject.toChatDto(): ChatDto {
        return ChatDto(
            id = getString("id"),
            title = getString("title"),
            initials = optString("initials", getString("title").initials()),
            lastMessage = optStringOrNull("lastMessage"),
            lastMessageTime = optStringOrNull("lastMessageTime"),
            unreadCount = optInt("unreadCount", 0),
            isOnline = optBoolean("isOnline", false),
        )
    }

    private fun JSONObject.toMessageDto(): MessageDto {
        return MessageDto(
            id = getLong("id"),
            chatId = getLong("chatId"),
            bodyText = optString("bodyText", ""),
            direction = optString("direction"),
            status = optString("status", "SENT"),
            isRead = optBoolean("isRead", false),
            createdAt = optString("createdAt"),
            sentAt = optStringOrNull("sentAt"),
            receivedAt = optStringOrNull("receivedAt"),
            attachments = optJSONArray("attachments").toMessageAttachmentDtos(),
        )
    }

    private fun JSONArray?.toMessageAttachmentDtos(): List<MessageAttachmentDto> {
        if (this == null) {
            return emptyList()
        }

        return buildList {
            for (index in 0 until length()) {
                add(getJSONObject(index).toMessageAttachmentDto())
            }
        }
    }

    private fun JSONObject.toMessageAttachmentDto(): MessageAttachmentDto {
        val url = optString("url")
        val id = if (has("id")) {
            optLong("id")
        } else {
            url.hashCode().toLong()
        }
        return MessageAttachmentDto(
            id = id,
            url = url.toAbsoluteUrl(),
            fileName = optStringOrNull("fileName"),
            contentType = optString("contentType", "application/octet-stream"),
            sizeBytes = optLong("sizeBytes", 0L),
            createdAt = optString("createdAt"),
        )
    }

    private fun JSONObject.toProfileDto(): ProfileDto {
        return ProfileDto(
            id = getString("id"),
            name = getString("name"),
            username = getString("username"),
            email = getString("email"),
            birthday = optStringOrNull("birthday"),
            bio = optStringOrNull("bio"),
            avatarUrl = optStringOrNull("avatarUrl")?.toAbsoluteUrl(),
            isOnline = optBoolean("isOnline", false),
        )
    }

    private fun JSONObject.toSettingsDto(): SettingsDto {
        return SettingsDto(
            language = optString("language", "en"),
            hasMailAccessToken = optBoolean("hasMailAccessToken", false),
            smtpHost = optStringOrNull("smtpHost"),
            smtpPort = optIntOrNull("smtpPort"),
            imapHost = optStringOrNull("imapHost"),
            imapPort = optIntOrNull("imapPort"),
        )
    }

    private fun JSONObject.optStringOrNull(name: String): String? {
        return if (isNull(name)) null else optString(name).ifBlank { null }
    }

    private fun JSONObject.optIntOrNull(name: String): Int? {
        return if (isNull(name) || !has(name)) null else optInt(name)
    }

    private fun String.toAbsoluteUrl(): String {
        return if (startsWith("http://") || startsWith("https://")) {
            this
        } else {
            baseUrl.trimEnd('/') + "/" + trimStart('/')
        }
    }

    private fun String.initials(): String {
        return split(" ")
            .filter(String::isNotBlank)
            .take(2)
            .joinToString("") { it.first().uppercase() }
            .ifBlank { "P" }
    }

    private fun jsonObject(vararg entries: Pair<String, Any?>): JSONObject {
        return JSONObject().apply {
            entries.forEach { (key, value) ->
                put(key, value)
            }
        }
    }

    private fun String?.hasBearerToken(): Boolean = !isNullOrBlank()

    private fun OutputStream.writeMultipartField(boundary: String, name: String, value: String) {
        write("--$boundary\r\n".toByteArray())
        write("Content-Disposition: form-data; name=\"$name\"\r\n\r\n".toByteArray())
        write(value.toByteArray(Charsets.UTF_8))
        write("\r\n".toByteArray())
    }

    private fun OutputStream.writeMultipartFile(
        boundary: String,
        fieldName: String,
        attachment: RemoteOutgoingAttachmentDto,
    ) {
        val safeFileName = attachment.fileName.replace("\"", "")
        write("--$boundary\r\n".toByteArray())
        write(
            "Content-Disposition: form-data; name=\"$fieldName\"; filename=\"$safeFileName\"\r\n"
                .toByteArray()
        )
        write("Content-Type: ${attachment.contentType}\r\n\r\n".toByteArray())
        write(attachment.bytes)
        write("\r\n".toByteArray())
    }

    private fun JSONObject.sanitizedForLog(): String {
        return sanitizedJsonObject().toString().safePreview(maxLength = 2_000)
    }

    private fun JSONObject.sanitizedJsonObject(): JSONObject {
        return JSONObject().also { sanitized ->
            val keys = keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val value = opt(key)
                sanitized.put(key, if (key.isSensitiveLogKey()) REDACTED else value.sanitizedJsonValue())
            }
        }
    }

    private fun JSONArray.sanitizedJsonArray(): JSONArray {
        return JSONArray().also { sanitized ->
            for (index in 0 until length()) {
                sanitized.put(opt(index).sanitizedJsonValue())
            }
        }
    }

    private fun Any?.sanitizedJsonValue(): Any? {
        return when (this) {
            is JSONObject -> sanitizedJsonObject()
            is JSONArray -> sanitizedJsonArray()
            else -> this
        }
    }

    private fun String.isSensitiveLogKey(): Boolean {
        return equals("password", ignoreCase = true) ||
            equals("code", ignoreCase = true) ||
            contains("token", ignoreCase = true) ||
            contains("authorization", ignoreCase = true)
    }

    private fun String.safePreview(maxLength: Int = 1_000): String {
        val singleLine = replace(Regex("\\s+"), " ").trim()
        return if (singleLine.length <= maxLength) singleLine else singleLine.take(maxLength) + "..."
    }
}

class PosterApiException(
    val statusCode: Int,
    override val message: String,
) : Exception(message)
