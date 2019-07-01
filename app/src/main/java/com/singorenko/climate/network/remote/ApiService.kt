package com.singorenko.climate.network.remote

import com.singorenko.climate.network.model.WeatherRequestModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    //example url that we request:
    //https://api.apixu.com/v1/forecast.json?key=11111a1a11aa111aaa111111111111&q=Moscow&days=4

    @GET("forecast.json?key")
    fun getWeatherRequest(
        @Query("key", encoded = true) key: String,
        @Query("q") q: String,
        @Query("days") days: Int
    ): Observable<WeatherRequestModel>
}