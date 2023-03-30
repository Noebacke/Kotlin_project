package com.example.weather.service

import com.example.weather.model.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    // Changer la requête pour récupérer les données que vous souhaitez intégrer dans votre UI
    @GET("/v1/forecast")
    suspend fun getWeather(
        @Query("longitude") longitude: Float = -0.57F,
        @Query("latitude") latitude: Float = 44.85F
    ): Response<Weather>
}