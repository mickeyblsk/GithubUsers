package com.higgs.githubusers.api

import okhttp3.OkHttpClient
import okhttp3.Request

class UserApi(private val searchString: String) {
    private val client: OkHttpClient
    private val request: Request

    init {
        val url = "https://api.github.com/users/$searchString"

        client = OkHttpClient()

        request = Request.Builder()
            .addHeader("Authorization", "Bearer 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")
            .url(url)
            .build()
    }

    fun getUserDetail(): String{
        val response = client.newCall(request).execute()
        return response.body()!!.string()
    }
}