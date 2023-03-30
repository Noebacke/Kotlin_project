package com.example.myapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.entity.WeatherCityEntity
import com.example.myapplication.repository.WeatherCityRepository
import kotlinx.coroutines.launch

class WeatherCityViewModel(private val repository: WeatherCityRepository) : ViewModel() {
    // Appeler le ViewModel dans une Activity pour récupérer les données
    val allCities: LiveData<List<WeatherCityEntity>> = repository.allCities.asLiveData()

    fun insert(city: WeatherCityEntity) = viewModelScope.launch {
        repository.insert(city)
    }
}