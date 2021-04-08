package com.example.loginscreen


    import com.example.loginscree.Country
    import retrofit2.Call
    import retrofit2.http.GET
    import retrofit2.http.Query

    interface interfaceRetrofit {

        //For everything

        @GET("everything")
        fun getEverything(
            @Query("q") q: String,
            @Query("from") from: String,
            @Query("sortBy") sortBy: String,
            @Query("apyKey") apiKey: String
        ): Call<Everything>


        //For country
        @GET("top-headlines")
        fun getCountry(
            @Query("country") country: String,
            @Query("apiKey") apiKey: String
        ): Call<Country>
    }