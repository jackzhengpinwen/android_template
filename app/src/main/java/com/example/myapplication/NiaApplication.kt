package com.example.myapplication

import android.app.Application
import com.example.autoloader.AutoServiceLoader
import com.example.core_common.base.BaseApplication
import com.example.sync.initializers.Sync
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NiaApplication: BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        Sync.initialize(context = this)
        AutoServiceLoader.getInstance().init();
    }
}