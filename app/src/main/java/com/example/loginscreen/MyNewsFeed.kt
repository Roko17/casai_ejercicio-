package com.example.loginscreen

import android.os.Bundle
import android.util.Log
import android.location.Geocoder
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_my_news_feed.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*



class MyNewsFeed : AppCompatActivity() {

    var lat: String? = null
    var lon: String? = null
    var flagButton: Int? = null

    val listaAllNews: MutableList<NoticiasDataClass> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_news_feed)

        lat = intent.getStringExtra("latitud")
        lon = intent.getStringExtra("longitud")
        GetInfo(lat, lon, 0)



    }

    fun initRecycler(){

        contenedor.layoutManager = LinearLayoutManager(this)
        val adapter = NewsAdapter(listaAllNews)
        contenedor.adapter = adapter

    }

    fun PressMyLocationButton(v:View){ if (flagButton != 0) GetInfo(lat, lon, 0) }

    fun PressInternationalButton(v:View){ if (flagButton != 1) GetInfo(lat, lon, 1) }



    fun GetInfo(lat: String?, lon: String?, flag: Int) {

        flagButton = flag

        val x = lat?.toDouble()
        val y = lon?.toDouble()
        val city = getCityName(x!!, y!!)

        //Log.e("----------", "--------------------------------------------------")

        /*Log.d("getInfo", "Your Lat:" + x)
        Log.d("getInfo", "Your Lon:" + y)
        Log.d("getInfo", "Your City:" + city)
        */

        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(interfaceRetrofit::class.java)

        val apiKey = "2ce274b68aa444a6ad39c9e8f9278c5e"

        val geocoder = Geocoder(this, Locale.getDefault())
        val address = geocoder.getFromLocation(x, y, 1)
        val countryCode: String = address.get(0).getCountryCode()
        //Log.e("code", countryCode)

        var call: Call<Country>? = null

        if (flag == 1){

            Toast.makeText(this, "international", Toast.LENGTH_SHORT).show()
             call = service.getRequest("us", apiKey)
        }
        else{

            Toast.makeText(this, "My Location", Toast.LENGTH_SHORT).show()
             call = service.getRequest(countryCode, apiKey)
        }

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


                    listaAllNews.clear()

                    for (article in articles!!) {
                        /* Log.e("Autor", article.author.toString())
                         Log.d("Titulo", article.title.toString())
                         Log.d("Log", article.description.toString())
                         Log.d("Log", article.publishedAt.toString())
                         Log.d("Log", article.content.toString())

                         */
                        var noticia: NoticiasDataClass? = null

                        if (article.author.toString() == "null"){
                            noticia = NoticiasDataClass("anonimo", article.title.toString(), article.content.toString(), article.publishedAt.toString())
                        }
                        else{
                            noticia = NoticiasDataClass(article.author.toString(), article.title.toString(), article.content.toString(), article.publishedAt.toString())
                        }

                        listaAllNews.add(noticia)
                    }

                    initRecycler()
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