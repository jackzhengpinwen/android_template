package com.example.core_common.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager

object NetWorkUtil {
    var num = 0

    enum class NetworkType {
        NETWORK_WIFI, NETWORK_4G, NETWORK_3G, NETWORK_2G, NETWORK_UNKNOWN,  //未知
        NETWORK_NO //无网
    }

    /**
     * 打开网络设置界面
     *
     * 3.0以下打开设置界面
     */
    fun openWirelessSettings(context: Context) {
        if (Build.VERSION.SDK_INT > 10) {
            context.startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } else {
            context.startActivity(Intent(Settings.ACTION_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    /**
     * 获取活动网络信息
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     *
     * @return NetworkInfo
     */
    private fun getActiveNetworkInfo(context: Context): NetworkInfo? {
        return (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
    }

    /**
     * 判断网络是否连接
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isConnected(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return info != null && info.isConnected
    }

    /**
     * 判断网络是否可用
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     *
     * @return `true`: 可用<br></br>`false`: 不可用
     */
    fun isAvailable(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return info != null && info.isAvailable
    }

    /**
     * Android 判断是否能真正上网(避免连入wifi无网的状态)
     */
//    public static final void ping(isRealeNetWork isRealeNetWork) {
//
//        new Thread(() -> {
//            try {
//                URL url = new URL("http://android.zhsd168.com/v2");
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("GET");
//                conn.setReadTimeout(1000);
//                conn.setConnectTimeout(1000);
//                if (conn.getResponseCode() == 200) {
//                    isRealeNetWork.hasNet();
//                } else {
//                    isRealeNetWork.noNet();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                isRealeNetWork.noNet();
//            }
//        }).start();
//
//    }
//
//    public interface isRealeNetWork {
//        void hasNet();
//
//        void noNet();
//    }


    /**
     * Android 判断是否能真正上网(避免连入wifi无网的状态)
     */
    //    public static final void ping(isRealeNetWork isRealeNetWork) {
    //
    //        new Thread(() -> {
    //            try {
    //                URL url = new URL("http://android.zhsd168.com/v2");
    //                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    //                conn.setRequestMethod("GET");
    //                conn.setReadTimeout(1000);
    //                conn.setConnectTimeout(1000);
    //                if (conn.getResponseCode() == 200) {
    //                    isRealeNetWork.hasNet();
    //                } else {
    //                    isRealeNetWork.noNet();
    //                }
    //            } catch (Exception e) {
    //                e.printStackTrace();
    //                isRealeNetWork.noNet();
    //            }
    //        }).start();
    //
    //    }
    //
    //    public interface isRealeNetWork {
    //        void hasNet();
    //
    //        void noNet();
    //    }
    /**
     * 判断移动数据是否打开
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    fun getDataEnabled(context: Context): Boolean {
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val getMobileDataEnabledMethod = tm.javaClass.getDeclaredMethod("getDataEnabled")
            if (null != getMobileDataEnabledMethod) {
                return getMobileDataEnabledMethod.invoke(tm) as Boolean
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }


    /**
     * 判断网络是否是4G
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    fun is4G(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return info != null && info.isAvailable && info.subtype == TelephonyManager.NETWORK_TYPE_LTE
    }

    /**
     * 判断wifi是否打开
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>`
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    fun getWifiEnabled(context: Context): Boolean {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.isWifiEnabled
    }

    /**
     * 打开或关闭wifi
     *
     * 需添加权限 `<uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>`
     *
     * @param enabled `true`: 打开<br></br>`false`: 关闭
     */
    fun setWifiEnabled(enabled: Boolean, context: Context) {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (enabled) {
            if (!wifiManager.isWifiEnabled) {
                wifiManager.isWifiEnabled = true
            }
        } else {
            if (wifiManager.isWifiEnabled) {
                wifiManager.isWifiEnabled = false
            }
        }
    }

    /**
     * 判断wifi是否连接状态
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     *
     * @return `true`: 连接<br></br>`false`: 未连接
     */
    fun isWifiConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm != null && cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * 判断wifi数据是否可用
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>`
     *
     * 需添加权限 `<uses-permission android:name="android.permission.INTERNET"/>`
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isWifiAvailable(context: Context): Boolean {
        return getWifiEnabled(context) && isAvailable(context)
    }

    /**
     * 获取网络运营商名称
     *
     * 中国移动、如中国联通、中国电信
     *
     * @return 运营商名称
     */
    fun getNetworkOperatorName(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm?.networkOperatorName
    }

    private const val NETWORK_TYPE_GSM = 16
    private const val NETWORK_TYPE_TD_SCDMA = 17
    private const val NETWORK_TYPE_IWLAN = 18

    /**
     * 获取当前网络类型
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     *
     * @return 网络类型
     *
     *  * [NetWorkUtil.NetworkType.NETWORK_WIFI]
     *  * [NetWorkUtil.NetworkType.NETWORK_4G]
     *  * [NetWorkUtil.NetworkType.NETWORK_3G]
     *  * [NetWorkUtil.NetworkType.NETWORK_2G]
     *  * [NetWorkUtil.NetworkType.NETWORK_UNKNOWN]
     *  * [NetWorkUtil.NetworkType.NETWORK_NO]
     *
     */
    fun getNetworkType(context: Context): NetworkType {
        var netType = NetworkType.NETWORK_NO
        val info = getActiveNetworkInfo(context)
        if (info != null && info.isAvailable) {
            netType = if (info.type == ConnectivityManager.TYPE_WIFI) {
                NetworkType.NETWORK_WIFI
            } else if (info.type == ConnectivityManager.TYPE_MOBILE) {
                when (info.subtype) {
                    NETWORK_TYPE_GSM, TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> NetworkType.NETWORK_2G
                    NETWORK_TYPE_TD_SCDMA, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> NetworkType.NETWORK_3G
                    NETWORK_TYPE_IWLAN, TelephonyManager.NETWORK_TYPE_LTE -> NetworkType.NETWORK_4G
                    else -> {
                        val subtypeName = info.subtypeName
                        if (subtypeName.equals("TD-SCDMA", ignoreCase = true)
                            || subtypeName.equals("WCDMA", ignoreCase = true)
                            || subtypeName.equals("CDMA2000", ignoreCase = true)
                        ) {
                            NetworkType.NETWORK_3G
                        } else {
                            NetworkType.NETWORK_UNKNOWN
                        }
                    }
                }
            } else {
                NetworkType.NETWORK_UNKNOWN
            }
        }
        return netType
    }
}