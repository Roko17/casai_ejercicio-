package com.example.loginscreen



    import retrofit2.Call
    import retrofit2.http.GET
    import retrofit2.http.Query

    interface interfaceRetrofit {


        //For country
        @GET("top-headlines")
        fun getRequest(
            @Query("country") country: String,
            @Query("apiKey") apiKey: String
        ): Call<Country>
    }