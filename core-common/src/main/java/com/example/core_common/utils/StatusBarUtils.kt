package com.example.core_common.utils

import android.R
import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import java.lang.reflect.Field

object StatusBarUtils {
    var DEFAULT_COLOR = 0
    var DEFAULT_ALPHA = 0f

    /**
     * 设置状态栏背景颜色
     */
    fun setColor(activity: Activity, @ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor = color
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val systemContent = activity.findViewById<ViewGroup>(R.id.content)
            val statusBarView = View(activity)
            val lp = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity)
            )
            statusBarView.setBackgroundColor(color)
            systemContent.getChildAt(0).fitsSystemWindows = true
            systemContent.addView(statusBarView, 0, lp)
        }
    }

    fun immersive(activity: Activity) {
        immersive(activity, DEFAULT_COLOR, DEFAULT_ALPHA)
    }

    fun immersive(activity: Activity, color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        immersive(activity.window, color, alpha)
    }

    fun immersive(activity: Activity, color: Int) {
        immersive(activity.window, color, 1f)
    }

    fun immersive(window: Window) {
        immersive(window, DEFAULT_COLOR, DEFAULT_ALPHA)
    }

    fun immersive(window: Window, color: Int) {
        immersive(window, color, 1f)
    }

    fun immersive(window: Window, color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = mixtureColor(color, alpha)
            var systemUiVisibility = window.decorView.systemUiVisibility
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.decorView.systemUiVisibility = systemUiVisibility
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            setTranslucentView(window.decorView as ViewGroup, color, alpha)
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            var systemUiVisibility = window.decorView.systemUiVisibility
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.decorView.systemUiVisibility = systemUiVisibility
        }
    }

    /**
     * 创建假的透明栏
     */
    fun setTranslucentView(
        container: ViewGroup,
        color: Int,
        @FloatRange(from = 0.0, to = 1.0) alpha: Float
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val mixtureColor = mixtureColor(color, alpha)
            var translucentView = container.findViewById<View>(R.id.custom)
            if (translucentView == null && mixtureColor != 0) {
                translucentView = View(container.context)
                translucentView.id = R.id.custom
                val lp = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(container.context)
                )
                container.addView(translucentView, lp)
            }
            translucentView?.setBackgroundColor(mixtureColor)
        }
    }

    fun mixtureColor(color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
        val a = if (color and -0x1000000 == 0) 0xff else color ushr 24
        return color and 0x00ffffff or ((a * alpha).toInt() shl 24)
    }

    // ========================  状态栏字体颜色设置  ↓ ================================
    /**
     * 设置状态栏黑色字体图标
     */
    fun setStatusBarBlackText(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = activity.window
            val decorView = window.decorView
            decorView.systemUiVisibility =
                decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            return true
        }
        return false
    }

    /**
     * 设置状态栏白色字体图标
     */
    fun setStatusBarWhiteText(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = activity.window
            val decorView = window.decorView
            decorView.systemUiVisibility =
                decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            return true
        }
        return false
    }

    // ========================  状态栏字体颜色设置  ↑================================
    // 在某些机子上存在不同的density值，所以增加两个虚拟值
    private var sStatusBarHeight = -1
    private const val sVirtualDensity = -1f
    private const val STATUS_BAR_DEFAULT_HEIGHT_DP = 25 // 大部分状态栏都是25dp


    /**
     * 获取状态栏的高度。
     */
    fun getStatusBarHeight(context: Context): Int {
        if (sStatusBarHeight == -1) {
            initStatusBarHeight(context)
        }
        return sStatusBarHeight
    }

    private fun initStatusBarHeight(context: Context) {
        val clazz: Class<*>
        var obj: Any? = null
        var field: Field? = null
        try {
            clazz = Class.forName("com.android.internal.R\$dimen")
            obj = clazz.newInstance()
            if (DeviceUtils.isMeizu()) {
                try {
                    field = clazz.getField("status_bar_height_large")
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
            if (field == null) {
                field = clazz.getField("status_bar_height")
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        if (field != null && obj != null) {
            try {
                val id = field[obj].toString().toInt()
                sStatusBarHeight = context.resources.getDimensionPixelSize(id)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
        if (DeviceUtils.isTablet(context)
            && sStatusBarHeight > CommUtils.dip2px(STATUS_BAR_DEFAULT_HEIGHT_DP)
        ) {
            //状态栏高度大于25dp的平板，状态栏通常在下方
            sStatusBarHeight = 0
        } else {
            if (sStatusBarHeight <= 0) {
                sStatusBarHeight = if (sVirtualDensity == -1f) {
                    CommUtils.dip2px(STATUS_BAR_DEFAULT_HEIGHT_DP)
                } else {
                    (STATUS_BAR_DEFAULT_HEIGHT_DP * sVirtualDensity + 0.5f).toInt()
                }
            }
        }
    }


    // ========================  适配状态栏高度  ↓ ================================
    /**
     * 适配状态栏高度的View - 设置Padding
     */
    fun fitsStatusBarViewPadding(view: View) {
        //增加高度
        val lp = view.layoutParams
        lp.height += getStatusBarHeight(view.context)

        //设置PaddingTop
        view.setPadding(
            view.paddingLeft,
            view.paddingTop + getStatusBarHeight(view.context),
            view.paddingRight,
            view.paddingBottom
        )
    }

    /**
     * 适配状态栏高度的View - 设置Margin
     */
    fun fitsStatusBarViewMargin(view: View) {
        if (view.layoutParams is MarginLayoutParams) {

            //已经添加过了不要再次设置
            if (view.tag != null && view.tag == "fitStatusBar") {
                return
            }
            val layoutParams = view.layoutParams as MarginLayoutParams
            val marginTop = layoutParams.topMargin
            val setMarginTop = marginTop + getStatusBarHeight(view.context)
            view.tag = "fitStatusBar"
            layoutParams.topMargin = setMarginTop
            view.requestLayout()
        }
    }

    /**
     * 适配状态栏高度的View - 使用布局包裹
     */
    fun fitsStatusBarViewLayout(view: View) {
        val fitParent = view.parent
        if (fitParent != null) {
            if (fitParent is LinearLayout && (fitParent as ViewGroup).tag != null && (fitParent as ViewGroup).tag == "fitLayout") {
                //已经添加过了不要再次设置
                return
            }

            //给当前布局包装一个适应布局
            val fitGroup = fitParent as ViewGroup
            fitGroup.removeView(view)
            val fitLayout = LinearLayout(view.context)
            fitLayout.orientation = LinearLayout.VERTICAL
            fitLayout.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            fitLayout.tag = "fitLayout"

            //先加一个状态栏高度的布局
            val statusView = View(view.context)
            statusView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(view.context)
            )
            fitLayout.addView(statusView)
            val fitViewParams = view.layoutParams
            fitLayout.addView(view)
            fitGroup.addView(fitLayout)
        }
    }
}