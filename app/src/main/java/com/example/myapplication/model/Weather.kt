package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

// Changer le modèle pour récupérer les données souhaitées dans votre UI
data class Weather(
    val latitude: Float,
    val longitude: Float,
    val timezone: String,
    @SerializedName("timezone_abbreviation") val timezoneAbbrevation: String,
    val elevation: Float,
    var current_weather: CurrentWeather,
)