package com.example.core_common.utils

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object ThreadPoolUtils {
    private lateinit var mCachedThreadPool: ExecutorService

    fun init() {
        mCachedThreadPool = Executors.newCachedThreadPool()
    }

    /**
     * 初始化线程池，全局通用缓存线程池
     */
    fun getCachedThreadPool(): ExecutorService {
        return mCachedThreadPool
    }

}