package com.example.core_network

import com.example.core_network.model.NetworkAuthor

interface NiaNetworkDataSource {
    suspend fun getAuthors(ids: List<String>? = null): List<NetworkAuthor>
}