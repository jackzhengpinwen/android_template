package com.example.sync.initializers

import android.content.Context
import android.os.Environment
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import androidx.work.WorkManagerInitializer
import com.example.core_common.Logger
import com.example.core_common.log.LogUtil
import com.example.core_common.log.interceptor.Log2FileInterceptor
import com.example.core_common.log.interceptor.LogDecorateInterceptor
import com.example.core_common.log.interceptor.LogPrintInterceptor
import com.example.sync.workers.SyncWorker
import java.io.File

object Sync {
    fun initialize(context: Context) {
        AppInitializer.getInstance(context)
            .initializeComponent(SyncInitializer::class.java)
    }
}

// This name should not be changed otherwise the app may have concurrent sync requests running
private const val SyncWorkName = "SyncWorkName"

class SyncInitializer : Initializer<Sync> {

    override fun create(context: Context): Sync {
        WorkManager.getInstance(context).apply {
            enqueueUniqueWork(
                SyncWorkName,
                ExistingWorkPolicy.KEEP,
                SyncWorker.startUpSyncWork()
            )
        }
        return Sync
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(WorkManagerInitializer::class.java, LoggerInitializer::class.java)
}

class LoggerInitializer: Initializer<String> {
    override fun create(context: Context): String {
        // log by me
        Logger.initialize(context)

        // log by others
        val logPath = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
            context.applicationContext.getExternalFilesDir("log")?.absolutePath
                ?: (context.applicationContext.filesDir.absolutePath + "/log/")
        else
            context.applicationContext.filesDir.absolutePath + "/log/"

        val dir = File(logPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        LogUtil.addInterceptor(LogDecorateInterceptor(false))
        LogUtil.addInterceptor(LogPrintInterceptor(true))
        LogUtil.addInterceptor(Log2FileInterceptor.getInstance(logPath, true))
        return "Logger"
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()

}