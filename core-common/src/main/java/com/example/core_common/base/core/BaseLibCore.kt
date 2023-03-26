package com.example.core_common.base.core

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.os.Environment
import android.os.Handler
import androidx.core.content.ContextCompat
import com.example.core_common.BuildConfig
import com.example.core_common.R
import com.example.core_common.receiver.ConnectivityReceiver
import com.example.core_common.utils.CommUtils
import com.example.core_common.utils.ThreadPoolUtils
import com.example.core_common.view.global.Gloading
import com.example.core_common.view.titlebar.EasyTitleBar
import java.io.File

object BaseLibCore {
    /**
     * 初始化全局工具类和图片加载引擎
     */
    fun init(context: Context, handler: Handler?, mainThread: Int) {

        //CommUtil初始化
        CommUtils.init(context, handler, mainThread)

        //初始化全局通用的线程池
        ThreadPoolUtils.init()

        //EaseTitleBar的初始化
        EasyTitleBar.init()
            .backIconRes(R.drawable.back_black)
            .backgroud(Color.WHITE)
            .titleSize(18)
            .showLine(false)
            .lineHeight(1)
            .menuImgSize(23)
            .menuTextSize(16)
            .lineColor(ContextCompat.getColor(context.applicationContext, R.color.divider_color))
            .titleColor(ContextCompat.getColor(context.applicationContext, R.color.black))
            .viewPadding(10)
            .titleBarHeight(46)

    }

    /**
     * 注册网络监听
     */
    fun registerNetworkObserver(application: Application?) {
        ConnectivityReceiver.registerReceiver(application!!)
    }

    fun unregisterNetworkObserver(application: Application?) {
        ConnectivityReceiver.unregisterReceiver(application!!)
    }
}