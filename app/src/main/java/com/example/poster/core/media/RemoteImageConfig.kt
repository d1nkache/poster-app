package com.example.poster.core.media

import android.content.Context
import android.util.Log
import coil.request.ImageRequest
import com.example.poster.BuildConfig

object RemoteImageConfig {
    private const val TAG = "PosterApi"
    private const val API_NINJAS_KEY_HEADER = "X-Api-Key"
    private const val API_NINJAS_ACCEPT_HEADER = "Accept"
    private const val API_NINJAS_ACCEPT_VALUE = "image/jpg"

    val randomImageUrl: String = BuildConfig.API_NINJAS_RANDOM_IMAGE_URL

    fun buildImageModel(
        context: Context,
        remoteUrl: String,
    ): Any? {
        if (requiresApiNinjasHeaders(remoteUrl) && BuildConfig.API_NINJAS_API_KEY.isBlank()) {
            Log.w(TAG, "Request skipped: GET $remoteUrl reason=missing_api_ninjas_key")
            return null
        }

        Log.d(
            TAG,
            "Request: GET $remoteUrl auth=false apiNinjasHeaders=${requiresApiNinjasHeaders(remoteUrl)}",
        )

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
