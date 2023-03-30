package com.example.myapplication.service

import com.example.myapplication.model.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    // Changer la requête pour récupérer les données que vous souhaitez intégrer dans votre UI

    @GET("/v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Float,
        @Query("longitude") longitude: Float,
        @Query("current_weather") current_weather: String = "true",
    ): Response<Weather>
}