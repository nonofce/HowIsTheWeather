package com.nonofce.android.howistheweather.data

import com.nonofce.android.howistheweather.data.response.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather?units=metric&APPID=8b1858d026e5b45100014dc1cec20b30")
    suspend fun getWeatherInformation(@Query("id") id: String): WeatherResponse

}