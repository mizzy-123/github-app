package com.dicoding.githubapp.api

import com.dicoding.githubapp.api.response.DataDetail
import com.dicoding.githubapp.api.response.DataFollowers
import com.dicoding.githubapp.api.response.DataFollowing
import com.dicoding.githubapp.api.response.DataSearch
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    suspend fun getSearch(@Header("Authorization") token: String, @Query("q") userName: String): DataSearch

    @GET("users/{username}")
    suspend fun getDetail(@Header("Authorization") token: String, @Path("username") userName: String): DataDetail

    @GET("users/{username}/followers")
    suspend fun getFollowers(@Header("Authorization") token: String, @Path("username") userName: String): List<DataFollowers>

    @GET("users/{username}/following")
    suspend fun getFollowing(@Header("Authorization") token: String, @Path("username") userName: String): List<DataFollowing>
}