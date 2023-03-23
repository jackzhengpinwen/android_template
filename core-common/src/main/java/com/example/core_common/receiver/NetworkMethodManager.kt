package com.example.core_common.receiver

import com.example.core_common.utils.NetWorkUtil
import java.lang.reflect.Method

class NetworkMethodManager {
    private var type //参数类型
            : Class<*>? = null

    private var networkType //网络类型
            : NetWorkUtil.NetworkType? = null

    private var method //方法对象
            : Method? = null

    constructor() {}

    constructor(
        type: Class<*>?,
        networkType: NetWorkUtil.NetworkType?,
        method: Method?
    ) {
        this.type = type
        this.networkType = networkType
        this.method = method
    }

    fun getType(): Class<*>? {
        return type
    }

    fun setType(type: Class<*>?) {
        this.type = type
    }

    fun getNetworkType(): NetWorkUtil.NetworkType? {
        return networkType
    }

    fun setNetworkType(networkType: NetWorkUtil.NetworkType?) {
        this.networkType = networkType
    }

    fun getMethod(): Method? {
        return method
    }

    fun setMethod(method: Method?) {
        this.method = method
    }
}