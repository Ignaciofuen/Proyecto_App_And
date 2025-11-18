package com.myapplication.data.remote

import com.myapplication.data.model.Post
import retrofit2.http.GET


interface ExternalApiService {

    @GET("/posts")
    suspend fun getPosts(): List<Post>


}