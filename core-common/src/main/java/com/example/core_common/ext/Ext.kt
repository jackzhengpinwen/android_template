package com.example.core_common.ext

import java.lang.reflect.ParameterizedType

fun <VM> getVMCls(cls: Any): VM {
    return (cls.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
}