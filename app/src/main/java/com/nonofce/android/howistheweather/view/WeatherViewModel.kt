package com.nonofce.android.howistheweather.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.nonofce.android.howistheweather.domain.CurrentWeather
import com.nonofce.android.howistheweather.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    /*
    val weatherInformation = liveData(Dispatchers.IO) {
        val weatherResponse = repository.getWeatherInformation("3553478")
        emit(weatherResponse)
    }
    */

    fun getWeatherInformation(cityId: String): LiveData<CurrentWeather> {
        return liveData(Dispatchers.IO) {
            try {

                val weatherResponse = repository.getWeatherInformation(cityId)
                emit(CurrentWeather(weatherResponse))
            } catch (e: Exception) {
                Log.e("getWeatherInformation", e.message)
                emit(CurrentWeather(null, "ERROR", e.message!!))
            }
        }
    }

}