package com.myapplication.repository
import com.myapplication.data.model.Post
import com.myapplication.data.remote.ExternalRetrofitInstance

class PostRepository {

    private val api = ExternalRetrofitInstance.api

    suspend fun getPosts(): List<Post> {
        return api.getPosts()
    }
}