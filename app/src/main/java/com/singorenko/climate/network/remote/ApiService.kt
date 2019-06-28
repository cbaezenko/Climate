package com.singorenko.climate.network.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

import com.singorenko.climate.network.model.WeatherRequestModel

interface ApiService {

    @GET("/latest")
    fun getWeatherRequest(
        @Query("base", encoded = true) base: String
    ): Observable<WeatherRequestModel>
}