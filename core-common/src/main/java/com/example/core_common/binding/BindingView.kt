package com.example.core_common.binding

import android.view.View
import androidx.databinding.BindingAdapter
import com.example.core_common.ext.click

/**
 * 设置控件的隐藏与显示
 */
@BindingAdapter("isVisibleGone")
fun isVisibleGone(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
}

/**
 * 点击事件防抖动的点击
 */
@BindingAdapter("clicks")
fun clicks(view: View, action: () -> Unit) {
    view.click { action() }
}