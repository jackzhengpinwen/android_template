package com.example.core_common.utils

import android.app.Application
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Process
import android.view.View
import androidx.core.content.ContextCompat

object CommUtils {
    private var mApplication: Application? = null
    private var mHandler: Handler? = null
    private var mMainTheadId = 0

    fun init(application: Application?, handler: Handler?, mainTheadId: Int) {
        mApplication = application
        mHandler = handler
        mMainTheadId = mainTheadId
    }

    //---------------------初始化Application定义的方法-----------------------------------
    fun getContext(): Context {
        return mApplication!!.applicationContext
    }

    fun getHandler(): Handler? {
        return mHandler
    }

    fun getMainThreadId(): Int {
        return mMainTheadId
    }


    //------------------------获取各种资源----------------------------------------
    fun getString(id: Int): String? {
        return getContext().resources.getString(id)
    }

    fun getStringArray(id: Int): Array<String?>? {
        return getContext().resources.getStringArray(id)
    }

    fun getIntArray(id: Int): IntArray? {
        return getContext().resources.getIntArray(id)
    }

    fun getDrawable(id: Int): Drawable? {
        return ContextCompat.getDrawable(getContext(), id)
    }

    fun getColor(id: Int): Int {
        return ContextCompat.getColor(getContext(), id)
    }

    fun getColorStateList(id: Int): ColorStateList? {
        return ContextCompat.getColorStateList(getContext(), id)
    }

    fun getDimens(id: Int): Int {
        return getContext().resources.getDimensionPixelSize(id)
    }

    //--------------------px和dip之间的转换-----------------------------------------------
    fun dip2px(dip: Int): Int {
        val density = getContext().resources.displayMetrics.density
        return (dip * density + 0.5f).toInt()
    }

    fun px2dip(px: Int): Float {
        val density = getContext().resources.displayMetrics.density
        return px / density
    }

    fun dip2px(dip: Float): Float {
        val density = getContext().resources.displayMetrics.density
        return dip * density + 0.5f
    }

    fun px2dip(px: Float): Float {
        val density = getContext().resources.displayMetrics.density
        return px / density
    }


    //-------------------加载布局文件-------------------------------------------------
    fun inflate(id: Int): View? {
        return View.inflate(getContext(), id, null)
    }

    //-------------------是否运行在主线程 -----------------------------------------------
    fun isRunOnUIThread(): Boolean {
        val myTid = Process.myTid()
        return if (myTid == getMainThreadId()) {
            true
        } else false
    }

    //运行在主线程
    fun runOnUIThread(runnable: Runnable) {
        if (isRunOnUIThread()) {
            runnable.run()
        } else {
            getHandler()!!.post(runnable)
        }
    }
}