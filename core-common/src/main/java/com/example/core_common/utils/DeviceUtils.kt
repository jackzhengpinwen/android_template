package com.example.core_common.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.ActivityManager
import android.app.AppOpsManager
import android.content.Context
import android.content.res.Configuration
import android.os.Binder
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.lang.reflect.Method
import java.util.*
import java.util.regex.Pattern

object DeviceUtils {
    private const val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
    private const val KEY_FLYME_VERSION_NAME = "ro.build.display.id"
    private const val FLYME = "flyme"
    private const val ZTEC2016 = "zte c2016"
    private const val ZUKZ1 = "zuk z1"
    private const val ESSENTIAL = "essential"
    private val MEIZUBOARD = arrayOf("m9", "M9", "mx", "MX")
    private var sMiuiVersionName: String? = null
    private var sFlymeVersionName: String? = null
    private var sIsTabletChecked = false
    private var sIsTabletValue = false
    private val BRAND = Build.BRAND.lowercase(Locale.getDefault())

    init {
        val properties = Properties()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // android 8.0，读取 /system/uild.prop 会报 permission denied
            var fileInputStream: FileInputStream? = null
            try {
                fileInputStream =
                    FileInputStream(File(Environment.getRootDirectory(), "build.prop"))
                properties.load(fileInputStream)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        var clzSystemProperties: Class<*>? = null
        try {
            clzSystemProperties = Class.forName("android.os.SystemProperties")
            val getMethod = clzSystemProperties.getDeclaredMethod("get", String::class.java)
            // miui
            sMiuiVersionName = getLowerCaseName(
                    properties,
                    getMethod,
                    KEY_MIUI_VERSION_NAME
                )
            //flyme
            sFlymeVersionName =
                getLowerCaseName(
                    properties,
                    getMethod,
                    KEY_FLYME_VERSION_NAME
                )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun _isTablet(context: Context): Boolean {
        return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >=
                Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    /**
     * 判断是否为平板设备
     */
    fun isTablet(context: Context): Boolean {
        if (sIsTabletChecked) {
            return sIsTabletValue
        }
        sIsTabletValue = _isTablet(context)
        sIsTabletChecked = true
        return sIsTabletValue
    }

    /**
     * 判断是否是flyme系统
     */
    fun isFlyme(): Boolean {
        return !TextUtils.isEmpty(sFlymeVersionName) && sFlymeVersionName!!.contains(FLYME)
    }

    /**
     * 判断是否是MIUI系统
     */
    fun isMIUI(): Boolean {
        return !TextUtils.isEmpty(sMiuiVersionName)
    }

    fun isMIUIV5(): Boolean {
        return "v5" == sMiuiVersionName
    }

    fun isMIUIV6(): Boolean {
        return "v6" == sMiuiVersionName
    }

    fun isMIUIV7(): Boolean {
        return "v7" == sMiuiVersionName
    }

    fun isMIUIV8(): Boolean {
        return "v8" == sMiuiVersionName
    }

    fun isMIUIV9(): Boolean {
        return "v9" == sMiuiVersionName
    }

    fun isFlymeLowerThan(majorVersion: Int): Boolean {
        return isFlymeLowerThan(majorVersion, 0, 0)
    }

    fun isFlymeLowerThan(majorVersion: Int, minorVersion: Int, patchVersion: Int): Boolean {
        var isLower = false
        if (sFlymeVersionName != null && sFlymeVersionName != "") {
            try {
                val pattern = Pattern.compile("(\\d+\\.){2}\\d")
                val matcher = pattern.matcher(sFlymeVersionName)
                if (matcher.find()) {
                    val versionString = matcher.group()
                    if (versionString.length > 0) {
                        val version =
                            versionString.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()
                        if (version.size >= 1) {
                            if (version[0].toInt() < majorVersion) {
                                isLower = true
                            }
                        }
                        if (version.size >= 2 && minorVersion > 0) {
                            if (version[1].toInt() < majorVersion) {
                                isLower = true
                            }
                        }
                        if (version.size >= 3 && patchVersion > 0) {
                            if (version[2].toInt() < majorVersion) {
                                isLower = true
                            }
                        }
                    }
                }
            } catch (ignore: Throwable) {
            }
        }
        return isMeizu() && isLower
    }


    fun isMeizu(): Boolean {
        return isPhone(MEIZUBOARD) || isFlyme()
    }

    /**
     * 判断是否为小米
     * https://dev.mi.com/doc/?p=254
     */
    fun isXiaomi(): Boolean {
        return Build.MANUFACTURER.lowercase(Locale.getDefault()) == "xiaomi"
    }

    fun isVivo(): Boolean {
        return BRAND.contains("vivo") || BRAND.contains("bbk")
    }

    fun isOppo(): Boolean {
        return BRAND.contains("oppo")
    }

    fun isHuawei(): Boolean {
        return BRAND.contains("huawei") || BRAND.contains("honor")
    }

    fun isEssentialPhone(): Boolean {
        return BRAND.contains("essential")
    }


    /**
     * 判断是否为 ZUK Z1 和 ZTK C2016。
     * 两台设备的系统虽然为 android 6.0，但不支持状态栏icon颜色改变，因此经常需要对它们进行额外判断。
     */
    fun isZUKZ1(): Boolean {
        val board = Build.MODEL
        return board != null && board.lowercase(Locale.getDefault()).contains(ZUKZ1)
    }

    fun isZTKC2016(): Boolean {
        val board = Build.MODEL
        return board != null && board.lowercase(Locale.getDefault()).contains(ZTEC2016)
    }

    private fun isPhone(boards: Array<String>): Boolean {
        val board = Build.BOARD ?: return false
        for (board1 in boards) {
            if (board == board1) {
                return true
            }
        }
        return false
    }

    /**
     * 判断悬浮窗权限（目前主要用户魅族与小米的检测）。
     */
    fun isFloatWindowOpAllowed(context: Context): Boolean {
        val version = Build.VERSION.SDK_INT
        return if (version >= 19) {
            checkOp(context, 24) // 24 是AppOpsManager.OP_SYSTEM_ALERT_WINDOW 的值，该值无法直接访问
        } else {
            try {
                context.applicationInfo.flags and (1 shl 27) == 1 shl 27
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    @TargetApi(19)
    private fun checkOp(context: Context, op: Int): Boolean {
        val version = Build.VERSION.SDK_INT
        if (version >= Build.VERSION_CODES.KITKAT) {
            val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            try {
                val method = manager.javaClass.getDeclaredMethod(
                    "checkOp",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType,
                    String::class.java
                )
                val property =
                    method.invoke(manager, op, Binder.getCallingUid(), context.packageName) as Int
                return AppOpsManager.MODE_ALLOWED == property
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    private fun getLowerCaseName(p: Properties, get: Method, key: String): String? {
        var name = p.getProperty(key)
        if (name == null) {
            try {
                name = get.invoke(null, key) as String
            } catch (ignored: Exception) {
            }
        }
        if (name != null) name = name.lowercase(Locale.getDefault())
        return name
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String? {
        val androidID =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val id = androidID + Build.SERIAL
        return try {
            Md5Util.md5(id)
        } catch (e: Exception) {
            e.printStackTrace()
            id
        }
    }


    fun killProcess(killName: String) {
        // 获取一个ActivityManager 对象
        val activityManager = CommUtils.getContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        // 获取系统中所有正在运行的进程
        val appProcessInfos = activityManager
            .runningAppProcesses
        // 对系统中所有正在运行的进程进行迭代，如果进程名所要杀死的进程，则Kill掉
        for (appProcessInfo in appProcessInfos) {
            val processName = appProcessInfo.processName
            if (processName == killName) {
                killProcessByPid(appProcessInfo.pid)
            }
        }
    }

    /**
     * 根据要杀死的进程id执行Shell命令已达到杀死特定进程的效果
     *
     * @param pid
     */
    private fun killProcessByPid(pid: Int) {
        val command = "kill -9 $pid\n"
        val runtime = Runtime.getRuntime()
        val proc: Process
        try {
            proc = runtime.exec(command)
            if (proc.waitFor() != 0) {
                System.err.println("exit value = " + proc.exitValue())
            }
        } catch (e1: IOException) {
            e1.printStackTrace()
        } catch (e: InterruptedException) {
            System.err.println(e)
        }
    }
}