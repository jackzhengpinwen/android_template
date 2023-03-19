package com.example.core_network.retrofit.response

@kotlinx.serialization.Serializable
data class NetworkResponse<T>(
    val data: T
)
