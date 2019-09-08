package com.nonofce.android.howistheweather.domain

data class WorldCity(
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String
)