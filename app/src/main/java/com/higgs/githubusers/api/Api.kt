package com.higgs.githubusers.api

import com.higgs.githubusers.data.model.UserList
import com.higgs.githubusers.data.model.UserListResponse
import com.higgs.githubusers.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(@Query("q") query: String): Call<UserListResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(@Path("username") username: String): Call<UserResponse>


    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(@Path("username") username: String): Call<ArrayList<UserList>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(@Path("username") username: String): Call<ArrayList<UserList>>
}