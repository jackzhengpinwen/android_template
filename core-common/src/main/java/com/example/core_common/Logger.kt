package com.example.core_common

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.jetbrains.annotations.NonNls
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

class FileLogTree constructor(private val context: Context): Timber.Tree() {
    companion object {
        private const val ANDROID_LOG_DAY_FORMAT = "yyyy-MM-dd"
        private const val ANDROID_LOG_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss"
    }

    private val writeFile = AtomicReference<File>()
    private val accumulatedLogs = ConcurrentHashMap<String, String>()

    private val coroutineScope: CoroutineScope = CoroutineScope(
        Dispatchers.Main + SupervisorJob()
    )

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority < Log.DEBUG) {
            return
        }
        try {
            accumulatedLogs[convertLongToTime(
                System.currentTimeMillis()
            )] = " priority = $priority, $message"
            createLogFile()

        } catch (e: IOException) {
            Timber.e(" Error while logging into file: $e")
        }
    }

    private fun createLogFile() =
        coroutineScope.launch {
            writeFile.lazySet(context.createFile(fileName = convertLongToDay(System.currentTimeMillis())))
            writeToLogFile()
        }

    private suspend fun writeToLogFile() {
        val result = runCatching {
            writeFile.get().bufferedWriter().use {
                it.append(accumulatedLogs.toString())
            }
        }
        if (result.isFailure) {
            result.exceptionOrNull()?.printStackTrace()
        }}

    private fun convertLongToTime(long: Long): String {
        val date = Date(long)
        val format = SimpleDateFormat(ANDROID_LOG_TIME_FORMAT, Locale.US)
        return format.format(date)
    }

    private fun convertLongToDay(long: Long): String {
        val date = Date(long)
        val format = SimpleDateFormat(ANDROID_LOG_DAY_FORMAT, Locale.US)
        return format.format(date)
    }

    fun Context.createFile(fileName: String): File {
        val file = File(externalCacheDir, fileName)
        if (!file.exists()) {
            file.createNewFile()
        }
        return file
    }
}

object Logger {
    fun initialize(context: Context) {
        Timber.plant(FileLogTree(context = context))
    }

    fun v(@NonNls message: String?, vararg args: Any?) {
        Timber.v(message, args)
    }

    /** Log a verbose exception and a message with optional format args. */
    fun v(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        Timber.v(t, message, *args)
    }

    /** Log a verbose exception. */
    fun v(t: Throwable?) {
        Timber.v(t)
    }

    /** Log a debug message with optional format args. */
    fun d(@NonNls message: String?, vararg args: Any?) {
        Timber.d(message, *args)
    }

    /** Log a debug exception and a message with optional format args. */
    fun d(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        Timber.d(t, message, *args)
    }

    /** Log a debug exception. */
    fun d(t: Throwable?) {
        Timber.d(t)
    }

    /** Log an info message with optional format args. */
    fun i(@NonNls message: String?, vararg args: Any?) {
        Timber.i(message, *args)
    }

    /** Log an info exception and a message with optional format args. */
    fun i(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        Timber.i(t, message, *args)
    }

    /** Log an info exception. */
    fun i(t: Throwable?) {
        Timber.i(t)
    }

    /** Log a warning message with optional format args. */
    fun w(@NonNls message: String?, vararg args: Any?) {
        Timber.w(message, *args)
    }

    /** Log a warning exception and a message with optional format args. */
    fun w(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        Timber.w(t, message, *args)
    }

    /** Log a warning exception. */
    fun w(t: Throwable?) {
        Timber.w(t)
    }

    /** Log an error message with optional format args. */
    fun e(@NonNls message: String?, vararg args: Any?) {
        Timber.e(message, *args)
    }

    /** Log an error exception and a message with optional format args. */
    fun e(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        Timber.e(t, message, *args)
    }

    /** Log an error exception. */
    fun e(t: Throwable?) {
        Timber.e(t)
    }

    /** Log an assert message with optional format args. */
    fun wtf(@NonNls message: String?, vararg args: Any?) {
        Timber.wtf(message, *args)
    }

    /** Log an assert exception and a message with optional format args. */
    fun wtf(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        Timber.wtf(t, message, *args)
    }

    /** Log an assert exception. */
    fun wtf(t: Throwable?) {
        Timber.wtf(t)
    }

    /** Log at `priority` a message with optional format args. */
    fun log(priority: Int, @NonNls message: String?, vararg args: Any?) {
        Timber.log(priority, message, *args)
    }

    /** Log at `priority` an exception and a message with optional format args. */
    fun log(priority: Int, t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        Timber.log(priority, t, message, *args)
    }

    /** Log at `priority` an exception. */
    fun log(priority: Int, t: Throwable?) {
        Timber.log(priority, t)
    }
}