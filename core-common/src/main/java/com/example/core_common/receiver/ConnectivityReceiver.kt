package com.example.core_common.receiver

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.example.core_common.annotation.NetWork
import com.example.core_common.base.BaseApplication
import com.example.core_common.utils.NetWorkUtil
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class ConnectivityReceiver : BroadcastReceiver() {
    //Observer的通知集合对象，本质上是接口回调
    private val mObservers: MutableList<ConnectivityReceiverListener>? = ArrayList()
    private var lastTimeMilles: Long = 0
    private var LAST_TYPE: NetWorkUtil.NetworkType = NetWorkUtil.NetworkType.NETWORK_UNKNOWN

    private object InstanceHolder {
        internal val INSTANCE = ConnectivityReceiver()
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action) {
            val networkType: NetWorkUtil.NetworkType = NetWorkUtil.getNetworkType(context)
            val currentTimeMillis = System.currentTimeMillis()

            //如果这次和上次的相同，那么5秒之内 只能触发一次
            if (LAST_TYPE === networkType) {
                if (lastTimeMilles == 0L) {
                    doNotifyObserver(networkType)
                } else {
                    if (currentTimeMillis - lastTimeMilles > 5000) {
                        doNotifyObserver(networkType)
                    }
                }
            } else {
                doNotifyObserver(networkType)
            }

            //重新赋值最后一次的网络类型和时间戳
            lastTimeMilles = currentTimeMillis
            LAST_TYPE = networkType
        }
    }

    //具体去执行通知
    private fun doNotifyObserver(networkType: NetWorkUtil.NetworkType) {
        //收到变换网络的通知就通过遍历集合去循环回调接口
        notifyObservers(networkType)

        //通知注解类型
        notifyByAnnotation(networkType)

        //赋值Application全局的类型
        BaseApplication.networkType = networkType
    }

    /**
     * 通知所有的Observer网络状态变化
     */
    private fun notifyObservers(networkType: NetWorkUtil.NetworkType) {
        if (networkType === NetWorkUtil.NetworkType.NETWORK_NO || networkType === NetWorkUtil.NetworkType.NETWORK_UNKNOWN) {
            for (observer in mObservers!!) {
                observer.onNetworkConnectionChanged(false, networkType)
            }
        } else {
            for (observer in mObservers!!) {
                observer.onNetworkConnectionChanged(true, networkType)
            }
        }
    }

    /**
     * 通知注解的方法类型去调用方法
     */
    private fun notifyByAnnotation(networkType: NetWorkUtil.NetworkType) {
        val keySet: Set<Any> = mAnnotationNetWorkObservers.keys
        for (getter in keySet) {
            //获取当前类的全部@NetWork方法
            val networkMethodManagers: List<NetworkMethodManager> =
                mAnnotationNetWorkObservers[getter]!!
            if (!networkMethodManagers.isEmpty()) {
                for (manager in networkMethodManagers) {

                    //逐一匹配对应的
                    if (manager.getNetworkType() === NetWorkUtil.NetworkType.NETWORK_2G) {
                        if (networkType === NetWorkUtil.NetworkType.NETWORK_2G || networkType === NetWorkUtil.NetworkType.NETWORK_NO) {
                            invoke(manager, getter)
                        }
                    } else if (manager.getNetworkType() === NetWorkUtil.NetworkType.NETWORK_3G) {
                        if (networkType === NetWorkUtil.NetworkType.NETWORK_3G || networkType === NetWorkUtil.NetworkType.NETWORK_NO) {
                            invoke(manager, getter)
                        }
                    } else if (manager.getNetworkType() === NetWorkUtil.NetworkType.NETWORK_4G) {
                        if (networkType === NetWorkUtil.NetworkType.NETWORK_4G || networkType === NetWorkUtil.NetworkType.NETWORK_NO) {
                            invoke(manager, getter)
                        }
                    } else if (manager.getNetworkType() === NetWorkUtil.NetworkType.NETWORK_WIFI) {
                        if (networkType === NetWorkUtil.NetworkType.NETWORK_WIFI || networkType === NetWorkUtil.NetworkType.NETWORK_NO) {
                            invoke(manager, getter)
                        }
                    } else if (manager.getNetworkType() === NetWorkUtil.NetworkType.NETWORK_UNKNOWN) {
                        invoke(manager, getter)
                    } else if (manager.getNetworkType() === NetWorkUtil.NetworkType.NETWORK_NO) {
                        if (networkType === NetWorkUtil.NetworkType.NETWORK_NO) {
                            invoke(manager, getter)
                        }
                    }
                }
            }
        }
    }

    /**
     * 反射执行的具体注解方法
     */
    private operator fun invoke(manager: NetworkMethodManager, getter: Any) {
        val method: Method? = manager.getMethod()
        try {
            method?.invoke(getter)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }

    //通过这个接口回调出去
    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean, networkType: NetWorkUtil.NetworkType?)
    }

    companion object {
        //保存以注解方式定义的网络监听方法的Map集合
        private val mAnnotationNetWorkObservers: MutableMap<Any, List<NetworkMethodManager>?> =
            HashMap<Any, List<NetworkMethodManager>?>()

        /**
         * 注册网络监听
         */
        fun registerReceiver(application: Application) {
            val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            application.applicationContext.registerReceiver(InstanceHolder.INSTANCE, intentFilter)
        }

        /**
         * 取消网络监听
         */
        fun unregisterReceiver(application: Application) {
            application.applicationContext.unregisterReceiver(InstanceHolder.INSTANCE)
        }

        /**
         * 注册网络变化Observer
         */
        fun registerObserver(observer: ConnectivityReceiverListener) {
            if (observer == null) return
            if (!InstanceHolder.INSTANCE.mObservers!!.contains(observer)) {
                InstanceHolder.INSTANCE.mObservers.add(observer)
            }
        }

        /**
         * 取消网络变化Observer的注册
         */
        fun unregisterObserver(observer: ConnectivityReceiverListener?) {
            if (observer == null) return
            if (InstanceHolder.INSTANCE.mObservers == null) return
            InstanceHolder.INSTANCE.mObservers.remove(observer)
        }

        /**
         * 注册网络变化的annotation的方法对象
         */
        fun registerAnnotationObserver(`object`: Any) {
            var networkMethodManagers: List<NetworkMethodManager>? =
                mAnnotationNetWorkObservers[`object`]
            if (networkMethodManagers == null || networkMethodManagers.isEmpty()) {
                //以前没有注册过，开始添加
                networkMethodManagers = findAnnotationMethod(`object`)
                mAnnotationNetWorkObservers[`object`] =
                    networkMethodManagers
            }
        }

        /**
         * 找到类中的全部注解@NetWork的方法
         */
        private fun findAnnotationMethod(`object`: Any): List<NetworkMethodManager> {
            val networkMethodManagers: MutableList<NetworkMethodManager> =
                ArrayList<NetworkMethodManager>()
            val clazz: Class<*> = `object`.javaClass //获取当前对象的class对象
            val methods = clazz.methods //获取当前class内部全部的method方法
            for (method in methods) {
                val netWork: NetWork = method.getAnnotation(NetWork::class.java) ?: continue //循环判断取出内部的@NetWork注解

                //开始添加到集合
                val manager = NetworkMethodManager(null, netWork.netWorkType, method)
                networkMethodManagers.add(manager)
            }
            return networkMethodManagers
        }

        /**
         * 解绑网络变化的annotation的方法对象
         */
        fun unregisterAnnotationObserver(`object`: Any) {
            if (!mAnnotationNetWorkObservers.isEmpty()) {
                //如果不为空，直接移除
                mAnnotationNetWorkObservers.remove(`object`)
            }
        }
    }
}