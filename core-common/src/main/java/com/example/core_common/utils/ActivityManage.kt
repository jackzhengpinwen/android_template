package com.example.core_common.utils

import android.app.Activity
import android.content.Context
import java.util.*

object ActivityManage {
    private var activityStack: Stack<Activity?>? = null

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity?) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack!!.push(activity)
    }

    //获取activity栈
    fun getActivityStack(): Stack<Activity?>? {
        return activityStack
    }

    /**
     * 只是移除栈，不用结束Activity
     */
    fun removeActivity(activity: Activity?) {
        activityStack!!.remove(activity)
    }


    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity? {
        return activityStack!!.lastElement()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishCurrentActivity() {
        val activity = activityStack!!.pop()
        activity!!.finish()
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            activityStack!!.remove(activity)
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack!!) {
            if (activity!!.javaClass == cls) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        for (activity in activityStack!!) {
            activity?.finish()
        }
        activityStack!!.clear()
    }

    /**
     * 结束当前页面之前的全部页面
     */
    fun finishBeforActivity() {
        for (i in activityStack!!.indices) {
            val activity = activityStack!![i]
            activity?.let { finishActivity(it) }
        }
    }

    /**
     * 退出应用程序
     */
    fun AppExit(context: Context?) {
        try {
            finishAllActivity()
            //            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            manager.killBackgroundProcesses(context.getPackageName());
//            System.exit(0);
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}