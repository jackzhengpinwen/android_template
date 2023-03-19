package com.example.myapplication

import android.app.Application
import com.example.sync.initializers.Sync
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NiaApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Sync.initialize(context = this)
    }
}