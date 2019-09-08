package com.nonofce.android.howistheweather.domain

import com.nonofce.android.howistheweather.data.response.WeatherResponse

class CurrentWeather (var body: WeatherResponse?, var status: String = "OK", var message: String = "")