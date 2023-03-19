package com.example.core_network.retrofit.api

import com.example.core_network.model.NetworkAuthor
import com.example.core_network.retrofit.response.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitNiaNetworkApi {
    @GET(value = "authors")
    suspend fun getAuthors(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkAuthor>>
}