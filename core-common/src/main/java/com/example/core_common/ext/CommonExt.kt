package com.example.core_common.ext

import android.content.Context
import com.example.core_common.utils.CommUtils

/**
 *  通用扩展
 */

/**
 * 全局的Context
 */
fun Any.commContext(): Context {
    return CommUtils.getContext()
}