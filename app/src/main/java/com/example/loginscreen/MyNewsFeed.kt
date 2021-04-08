package com.example.loginscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.Intent
import android.location.Geocoder
import com.example.loginscree.Country
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class MyNewsFeed : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_news_feed)

        val lat: String? = intent.getStringExtra("latitud")
        val lon: String? = intent.getStringExtra("longitud")
        GetInfo(lat, lon)

    }

    fun GetInfo(lat: String?, lon: String?) {

        Log.e("latitud", lat)
        Log.e("longitud", lon)


        val x = lat?.toDouble()
        val y = lon?.toDouble()
        val city = getCityName(x!!, y!!)
        val cadena =
            "You Current Location is \nLat: " + x.toString() + "\nLon: " + y.toString() + "\n" + city
        //textViewGeo.text = cadena

        Log.d("Debug:", "Your Lat:" + x)
        Log.d("Debug:", "Your Lon:" + y)
        Log.d("Debug:", "Your City:" + city)


        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(interfaceRetrofit::class.java)

        val q = "Apple"
        val from = "2021-04-05"
        val sortBy = "popularity"
        val apiKey = "2ce274b68aa444a6ad39c9e8f9278c5e"

        val geocoder = Geocoder(this, Locale.getDefault())
        val address = geocoder.getFromLocation(x, y, 1)
        val countryCode: String = address.get(0).getCountryCode()
        Log.e("code", countryCode)

        //val call = service.getEverything(q, from, sortBy, apiKey)
        val call = service.getCountry(countryCode, apiKey)

        call.enqueue(object : Callback<Country> {
            override fun onResponse(
                call: Call<Country>,
                response: Response<Country>
            ) {
                if (response.code() == 200) {
                    val newsResponse = response.body()!!

                    val status = newsResponse.status
                    val totalResults = newsResponse.totalResults
                    val articles = newsResponse.articles

                    for (article in articles!!) {
                        Log.e("Log", article.author.toString())
                        Log.d("Log", article.title.toString())
                        Log.d("Log", article.description.toString())
                        Log.d("Log", article.publishedAt.toString())
                        Log.d("Log", article.content.toString())
                    }
                }
            }

            override fun onFailure(call: Call<Country>?, t: Throwable?) {
                TODO("Not yet implemented")
            }
        })

    }


    fun getCityName(lat: Double, long: Double): String {
        var cityName: String = ""
        var countryName = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat, long, 3)

        cityName = Adress.get(0).locality
        countryName = Adress.get(0).countryName
        //Log.d("Debug:","Your City: " + cityName + "\nYour Country " + countryName)
        return cityName
    }
}