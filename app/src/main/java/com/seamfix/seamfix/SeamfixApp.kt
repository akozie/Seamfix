package com.seamfix.seamfix

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SeamfixApp(): Application() {
    override fun onCreate() {
        super.onCreate()
    }
}