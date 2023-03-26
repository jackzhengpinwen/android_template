package com.example.core_common.view.titlebar

import android.content.Context
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView

object EasyUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }


    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    fun sp2px(context: Context, spValue: Int): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }


    fun getColorWithAlpha(alpha: Float, baseColor: Int): Int {
        val a = Math.min(255, Math.max(0, (alpha * 255).toInt())) shl 24
        val rgb = 0x00ffffff and baseColor
        return a + rgb
    }

    fun getColorWithRatio(ratio: Float, baseColor: Int): Int {
        val alpha = Math.min(1f, ratio)
        return getColorWithAlpha(alpha, baseColor)
    }


    fun addOnSrollListener(view: View, onSrollListener: OnSrollListener) {
        if (view is NestedScrollView) {
            view.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                onSrollListener.onSrollEvent(
                    scrollY
                )
            })
        } else if (view is RecyclerView) {
            (view as RecyclerView).addOnScrollListener(object : RecyclerView.OnScrollListener() {
                private var totalDy = 0
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    totalDy += dy
                    onSrollListener.onSrollEvent(totalDy)
                }
            })
        }
    }


    /**
     * 获取状态栏高度,在页面还没有显示出来之前
     *
     * @param a
     * @return
     */
    fun getStateBarHeight(a: Context): Int {
        var result = 0
        val resourceId = a.resources.getIdentifier(
            "status_bar_height",
            "dimen", "android"
        )
        if (resourceId > 0) {
            result = a.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }


    interface OnSrollListener {
        fun onSrollEvent(scrollY: Int)
    }
}