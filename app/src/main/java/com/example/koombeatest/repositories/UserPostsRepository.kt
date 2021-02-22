package com.example.koombeatest.repositories

import com.example.koombeatest.data.remote.Data
import com.example.koombeatest.data.remote.UserPosts
import com.example.koombeatest.utils.Resource

interface UserPostsRepository {

    suspend fun insertUserPosts(userPosts : UserPosts)

    suspend fun getLocalUserPosts() : Resource<UserPosts>

    suspend fun getRemoteUserPosts() : Resource<UserPosts>

    suspend fun getUserPosts() : Resource<UserPosts>

}