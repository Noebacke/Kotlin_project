package com.example.myapplication.views

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.database.CityRoomDatabase
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.entity.WeatherCityEntity
import com.example.myapplication.model.Weather
import com.example.myapplication.repository.WeatherCityRepository
import com.example.myapplication.retrofit.RetrofitBuilder
import com.example.myapplication.viewmodels.WeatherViewModel
import kotlinx.coroutines.*
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var weatherviewModel: WeatherViewModel
    var latitude =  44.84F
    var longitude = 0.56F

    private fun searchingButton(){
        longitude = binding.inputLongitude.text.toString().toFloat()
        latitude = binding.inputLatitude.text.toString().toFloat()
        setResultOfSearch()
    }

//    private fun supressDataOfTextLAtitude(){
//        binding.inputLatitude.text?.clear()
//    }
//
//    private fun supressDataOfTextLongitude(){
//        binding.inputLongitude.text?.clear()
//    }

    private fun setResultOfSearch(){
        val retrofitBuilder = RetrofitBuilder.apiService
        CoroutineScope(Dispatchers.IO).launch {
            val responseWeather = retrofitBuilder.getWeather(latitude,longitude)
            withContext(Dispatchers.Main) {
                try {
                    if (responseWeather.isSuccessful && null != responseWeather.body()) {
                        var content = responseWeather.body()
                        var valueOfFile = content?.current_weather?.weathercode.toString()
                        val filename = "w_$valueOfFile"
                        val resID = resources.getIdentifier(filename, "drawable", packageName)
                        println("CALL API : $content")
                        binding.img.setImageResource(resID)
                        binding.returnData.text = ("Longitude : " + content?.longitude.toString())
                        binding.returnData2.text = ("Latitude : " + content?.latitude.toString())
                        binding.returnData3.text = ("Heure : " + content?.current_weather?.time.toString())
                        binding.returnData4.text = ("Température : " + content?.current_weather?.temperature.toString()
                        + " °C")
                        binding.returnData5.text = ("Vitesse du vent : " + responseWeather.body()?.current_weather?.windspeed.toString() +
                        " km/h")
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

    @SuppressLint("DiscouragedApi")
    private fun setCurrentMeteo(time:String,meteo:String) {
        val value = meteo
        val filename = "w_$value"
        val resID = resources.getIdentifier(filename, "drawable", packageName)
        binding.img.setImageResource(resID)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var button = binding.eventClick
        button.setOnClickListener { searchingButton() }

//        var inputLat = binding.inputLatitude
//        var inputLong = binding.inputLongitude
//
//        inputLong.setOnFocusChangeListener { view, b -> supressDataOfTextLongitude() }
//        inputLat.setOnFocusChangeListener { view, b -> supressDataOfTextLAtitude() }

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)

        weatherviewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        val text = binding.returnData.text
        val text2 = binding.returnData2.text
        val text3 = binding.returnData3.text



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
    }


}