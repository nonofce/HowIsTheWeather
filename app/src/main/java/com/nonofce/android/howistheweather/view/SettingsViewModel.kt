package com.nonofce.android.howistheweather.view

import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.nonofce.android.howistheweather.domain.WorldCity
import org.json.JSONArray

class SettingsViewModel : ViewModel() {

    var cityList: MutableList<WorldCity> = mutableListOf()

    init {
        Log.d("SettingsViewModel--", "SettingsViewModel created")

    }

    override fun onCleared() {
        super.onCleared()
        Log.d("SettingsViewModel--", "SettingsViewModel cleared")
    }

    fun getListOfCities(assets: AssetManager) {
        if (cityList.isEmpty()) {
            val jsonFile = assets.open("city.short.list.json").bufferedReader().use {
                it.readText()
            }
            val cities = JSONArray(jsonFile)
            for (i in 0 until cities.length()) {
                val city = Gson().fromJson(cities[i].toString(), WorldCity::class.java)
                cityList.add(city)
            }

        }
    }
}