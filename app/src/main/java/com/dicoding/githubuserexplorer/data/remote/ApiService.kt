package com.dicoding.githubuserexplorer.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService{

    @GET ("search/users")
    fun getSearchUser (
        @Query ("q") username : String
    ): Call<GithubResponse>

    @GET ("users/{username}")
    fun getDetailUser (
        @Path("username") username: String
    ) : Call<DetailUserResponse>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET ("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}