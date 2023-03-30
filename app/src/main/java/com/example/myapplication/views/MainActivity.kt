package com.example.myapplication.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.database.CityRoomDatabase
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.entity.WeatherCityEntity
import com.example.myapplication.repository.WeatherCityRepository
import com.example.myapplication.retrofit.RetrofitBuilder
import com.example.myapplication.viewmodels.WeatherViewModel
import kotlinx.coroutines.*
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var weatherviewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)

        weatherviewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        val text = binding.returnData.id
        println("TEST : $text")



        // Tout le code en dessous, ne doit pas exister dans le MainActivity, il doit être présent dans le ViewModel
        // Database Room
        val applicationScope = CoroutineScope(SupervisorJob())
        val database = CityRoomDatabase.getDatabase(this, applicationScope)
        val repository = WeatherCityRepository(database.cityDao())
        applicationScope.launch(Dispatchers.IO) {
            repository.insert(WeatherCityEntity("Test"))
        }

        // Retrofit Create instance and Get Request

        // Il faut utiliser un ViewModel pour faire le call api
        val retrofitBuilder = RetrofitBuilder.apiService
        CoroutineScope(Dispatchers.IO).launch {
            val responseWeather = retrofitBuilder.getWeather()
            withContext(Dispatchers.Main) {
                try {
                    if (responseWeather.isSuccessful) {
                        println("CALL API : ${responseWeather.body()}")
                        binding.returnData.text = responseWeather.body()?.latitude.toString()
                        //Faire les modifications d'UI.
                    } else {
                        Toast.makeText(baseContext, "Error: ${responseWeather.code()}", Toast.LENGTH_LONG)
                    }
                } catch (e: HttpException) {
                    Toast.makeText(baseContext, "Exception: ${e.message()}", Toast.LENGTH_LONG)
                } catch (e: Throwable) {
                    Toast.makeText(baseContext, "Ooops: Something else went wrong", Toast.LENGTH_LONG)
                }
            }
        }
    }


}