package com.example.loginscreen


    import com.google.gson.annotations.SerializedName

    class Country {
        @SerializedName("status")
        var status: String? = "null"
        @SerializedName("totalResults")
        var totalResults: Int? = 0
        @SerializedName("articles")
        var articles: ArrayList<Article>? = null
    }

    class Article{
        @SerializedName("source")
        var sources: Source? = null
        @SerializedName("author")
        var author: String? = null
        @SerializedName("title")
        var title: String? = null
        @SerializedName("description")
        var description: String? = null
        @SerializedName("publishedAt")
        var publishedAt: String? = null
        @SerializedName("content")
        var content: String? = null

    }

    class Source{
        @SerializedName("id")
        var id: String? = null
        @SerializedName("name")
        var name: String? = null
    }

