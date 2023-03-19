package com.example.core_network.retrofit

//import com.example.core_network.BuildConfig
import com.example.core_network.NiaNetworkDataSource
import com.example.core_network.model.NetworkAuthor
import com.example.core_network.retrofit.api.RetrofitNiaNetworkApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

//private const val NiaBaseUrl = BuildConfig.BACKEND_URL

@Singleton
class RetrofitNiaNetwork @Inject constructor(
    networkJson: Json
): NiaNetworkDataSource {
    private val networkApi = Retrofit.Builder()
        .baseUrl("http://example.com")
        .client(
            OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                )
                .build()
        )
        .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(RetrofitNiaNetworkApi::class.java)


    override suspend fun getAuthors(ids: List<String>?): List<NetworkAuthor> {
        return networkApi.getAuthors(ids = ids).data
    }

}