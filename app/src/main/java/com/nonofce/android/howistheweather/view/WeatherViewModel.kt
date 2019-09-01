package com.nonofce.android.howistheweather.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.nonofce.android.howistheweather.data.response.WeatherResponse
import com.nonofce.android.howistheweather.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    val weatherInformation = liveData(Dispatchers.IO) {
        val weatherResponse = repository.getWeatherInformation("3553478")
        emit(weatherResponse)
    }

    fun getWeatherInformation(cityId: String): LiveData<WeatherResponse> {
        try{
            return liveData(Dispatchers.IO) {
                val weatherResponse = repository.getWeatherInformation(cityId)
                emit(weatherResponse)
            }
        }catch(e: Exception){
            Log.e("getWeatherInformation", e.message)
        }
        return MutableLiveData<WeatherResponse>()
    }

}