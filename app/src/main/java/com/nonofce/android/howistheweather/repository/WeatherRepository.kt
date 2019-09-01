package com.nonofce.android.howistheweather.repository

import com.nonofce.android.howistheweather.data.WeatherApi

class WeatherRepository(private val api: WeatherApi) {

    suspend fun getWeatherInformation(cityId: String) = api.getWeatherInformation(cityId)
}