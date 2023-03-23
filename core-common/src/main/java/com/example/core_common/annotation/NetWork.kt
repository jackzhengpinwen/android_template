package com.example.core_common.annotation

import com.example.core_common.utils.NetWorkUtil
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
) //定义在方法上面的注解 ，和EventBus的方式类似
@Retention(RetentionPolicy.RUNTIME) //定义为运行时，在jvm运行的过程中通过反射获取到注解
annotation class NetWork(open val netWorkType: NetWorkUtil.NetworkType = NetWorkUtil.NetworkType.NETWORK_NO)
