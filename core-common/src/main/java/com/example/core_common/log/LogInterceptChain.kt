package com.example.core_common.log

abstract class LogInterceptChain {
    var next: LogInterceptChain? = null

    open fun intercept(priority: Int, tag: String, logMsg: String?) {
        next?.intercept(priority, tag, logMsg)
    }
}