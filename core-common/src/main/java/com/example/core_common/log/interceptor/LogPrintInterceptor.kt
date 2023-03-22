package com.example.core_common.log.interceptor

import android.util.Log
import com.example.core_common.log.LogInterceptChain

/**
 * 使用Android Log 打印日志
 */
open class LogPrintInterceptor(private val isEnable: Boolean) : LogInterceptChain() {

    override fun intercept(priority: Int, tag: String, logMsg: String?) {
        if (isEnable) {
            Log.println(priority, tag, logMsg ?: "-")
        }
        super.intercept(priority, tag, logMsg)
    }
}