package com.nonofce.android.howistheweather.view

import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.nonofce.android.howistheweather.domain.CurrentWeather
import com.nonofce.android.howistheweather.domain.WorldCity
import com.nonofce.android.howistheweather.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import org.json.JSONArray

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    var currentWeather: CurrentWeather
    var numRequests: Int = 0
    var cityList: MutableList<WorldCity> = mutableListOf()
    var defaultCity: WorldCity? = null
    var currentSelection = 0
    var icon: String? = null

    init {
        currentWeather = CurrentWeather(null)
        Log.d("WeatherViewModel", "WeatherViewModel created")

    }
    /*
    val weatherInformation = liveData(Dispatchers.IO) {
        val weatherResponse = repository.getWeatherInformation("3553478")
        emit(weatherResponse)
    }
    */

    fun getWeatherInformation(cityId: String): LiveData<CurrentWeather> {
        numRequests++
        return liveData(Dispatchers.IO) {
            try {

                val weatherResponse = repository.getWeatherInformation(cityId)
                currentWeather = CurrentWeather(weatherResponse)
                emit(currentWeather)
            } catch (e: Exception) {
                Log.e("getWeatherInformation", e.message)
                currentWeather = CurrentWeather(null, "ERROR", e.message!!)
                emit(currentWeather)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("WeatherViewModel", "WeatherViewModel cleared")
    }

    fun getListOfCities(assets: AssetManager) {
        if (cityList.isEmpty()) {
            val jsonFile = assets.open("city.short.list.json").bufferedReader().use {
                it.readText()
            }
            val cities = JSONArray(jsonFile)
            for (i in 0 until cities.length()) {
                val city = Gson().fromJson(cities[i].toString(), WorldCity::class.java)
                if (city.id == 3553478) {
                    defaultCity = city
                }
                cityList.add(city)
            }

        }
    }


}