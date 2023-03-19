package com.example.core_network

import com.example.core_network.model.NetworkAuthor
import com.example.core_network.model.NetworkChangeList

interface NiaNetworkDataSource {
    suspend fun getAuthors(ids: List<String>? = null): List<NetworkAuthor>

    suspend fun getAuthorChangeList(after: Int? = null): List<NetworkChangeList>

}