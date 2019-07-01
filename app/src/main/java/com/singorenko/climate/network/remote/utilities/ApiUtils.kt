package com.singorenko.climate.network.remote.utilities

import com.singorenko.climate.network.remote.ApiService
import com.singorenko.climate.network.remote.RetrofitClient

class ApiUtils {
    companion object {
        fun getApiService(): ApiService {
            val baseUrl = "https://api.apixu.com/v1/"
            return RetrofitClient.create(baseUrl).create(ApiService::class.java)
        }
    }
}