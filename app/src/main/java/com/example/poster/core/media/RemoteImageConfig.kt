package com.example.poster.core.media

import android.content.Context
import coil.request.ImageRequest
import com.example.poster.BuildConfig

object RemoteImageConfig {
    private const val API_NINJAS_KEY_HEADER = "X-Api-Key"
    private const val API_NINJAS_ACCEPT_HEADER = "Accept"
    private const val API_NINJAS_ACCEPT_VALUE = "image/jpg"

    val randomImageUrl: String = BuildConfig.API_NINJAS_RANDOM_IMAGE_URL

    fun buildImageModel(
        context: Context,
        remoteUrl: String,
    ): Any? {
        if (requiresApiNinjasHeaders(remoteUrl) && BuildConfig.API_NINJAS_API_KEY.isBlank()) {
            return null
        }

        return ImageRequest.Builder(context)
            .data(remoteUrl)
            .crossfade(true)
            .apply {
                if (requiresApiNinjasHeaders(remoteUrl)) {
                    setHeader(API_NINJAS_KEY_HEADER, BuildConfig.API_NINJAS_API_KEY)
                    setHeader(API_NINJAS_ACCEPT_HEADER, API_NINJAS_ACCEPT_VALUE)
                }
            }
            .build()
    }

    private fun requiresApiNinjasHeaders(remoteUrl: String): Boolean {
        return remoteUrl.startsWith(randomImageUrl)
    }
}
