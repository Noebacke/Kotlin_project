package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    val temperature: Float,
    val windspeed: Float,
    val winddirection: Float,
    val weathercode: Int,
    var time: String
)
