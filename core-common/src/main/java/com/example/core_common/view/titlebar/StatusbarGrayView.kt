package com.example.core_common.view.titlebar

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.example.core_common.R
import com.example.core_common.utils.CommUtils

class StatusbarGrayView: View {
    constructor(context: Context?): super(context) {

    }

    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs) {

    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec) //得到宽度设置模式
        val widthSize = MeasureSpec.getSize(widthMeasureSpec) //得到宽度设置模式
        val heightMode = MeasureSpec.getMode(heightMeasureSpec) //得到高度设置模式

        //如果设置高度为wrap-content自适应 那么固定设置为状态栏高度
        if (heightMode == MeasureSpec.AT_MOST) {
            if (widthMode == MeasureSpec.EXACTLY) {
                setMeasuredDimension(widthSize, EasyUtil.getStateBarHeight(context))
            } else {
                setMeasuredDimension(1, EasyUtil.getStateBarHeight(context))
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val parent = parent as ViewGroup ?: return
        val drawable = parent.background
        if (drawable is ColorDrawable) {
            val color = drawable.color
            if (color == Color.WHITE || color == CommUtils.getColor(R.color.white)) {
                setBackgroundColor(if (Build.VERSION.SDK_INT < 23) CommUtils.getColor(R.color.status_bar_gray_bg) else Color.WHITE)
            } else {
                setBackgroundColor(Color.TRANSPARENT)
            }
        } else {
            setBackgroundColor(Color.TRANSPARENT)
        }
    }
}