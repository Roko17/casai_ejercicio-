package com.example.loginscreen

import com.example.loginscree.Article
import com.google.gson.annotations.SerializedName

class Everything {


        @SerializedName("status")
        var status: String? = "null"
        @SerializedName("totalResults")
        var totalResults: Int? = 0
        @SerializedName("articles")
        var articles: ArrayList<Article>? = null

}