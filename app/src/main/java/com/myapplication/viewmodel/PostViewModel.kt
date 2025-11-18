package com.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapplication.data.model.Post
import com.myapplication.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {


    private val repository = PostRepository()


    private val _posts = MutableStateFlow<List<Post>>(emptyList())

    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    init {
        fetchPosts()
    }


    private fun fetchPosts() {
        viewModelScope.launch {
            try {
                _posts.value = repository.getPosts()
            } catch (e: Exception) {
                println("Error al obtener posts: ${e.localizedMessage}")
            }
        }
    }
}