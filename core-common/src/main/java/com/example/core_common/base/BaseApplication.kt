package com.example.core_common.base

import android.app.Application
import android.content.Context
import android.os.Handler
import com.example.core_common.receiver.ConnectivityReceiver
import com.example.core_common.utils.NetWorkUtil
import com.google.gson.Gson

open class BaseApplication : Application() {

    //全局的静态Gson对象
    companion object {
        val mGson = Gson()
        lateinit var networkType: NetWorkUtil.NetworkType   //此变量会在网络监听中被动态赋值

        //检查当前是否有网络
        fun checkHasNet(): Boolean {
            return networkType != NetWorkUtil.NetworkType.NETWORK_NO && networkType != NetWorkUtil.NetworkType.NETWORK_UNKNOWN
        }

    }

    override fun onCreate() {
        super.onCreate()

        //获取到全局的网络状态
        networkType = NetWorkUtil.getNetworkType(this@BaseApplication.applicationContext)

        //全局的 CommUtil的初始化
//        BaseLibCore.init(this, Handler(), android.os.Process.myTid())

        //网络监听
//        BaseLibCore.registerNetworkObserver(this)
        ConnectivityReceiver.registerReceiver(this)

    }

    //Dex分包
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
//        MultiDex.install(this)
    }

    override fun onTerminate() {
        super.onTerminate()
//        BaseLibCore.unregisterNetworkObserver(this)
        ConnectivityReceiver.unregisterReceiver(this)
    }

}