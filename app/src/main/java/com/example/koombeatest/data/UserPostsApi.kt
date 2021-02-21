package com.example.koombeatest.data

import com.example.koombeatest.data.remote.UserPosts
import retrofit2.Response
import retrofit2.http.GET

interface UserPostsApi {

    @GET("/mt/api/posts")
    suspend fun getUserPosts() : Response<UserPosts>
}