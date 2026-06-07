package com.example.poster

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PosterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Application created")
    }

    private companion object {
        private const val TAG = "PosterApplication"
    }
}
