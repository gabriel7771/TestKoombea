package com.example.koombeatest.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koombeatest.data.remote.Data
import com.example.koombeatest.data.remote.UserPosts
import com.example.koombeatest.repositories.UserPostsRepository
import com.example.koombeatest.utils.Resource
import kotlinx.coroutines.launch
import timber.log.Timber

class UserPostsViewModel @ViewModelInject constructor(
    private val repository:UserPostsRepository
) : ViewModel() {

    private val _userPosts = MutableLiveData<Resource<UserPosts>>()
    val userPosts: LiveData<Resource<UserPosts>>
        get() = _userPosts

    init {
        refreshUserPosts()
    }

    fun refreshUserPosts(){
        //_userPosts.value = Resource.loading(null)
        viewModelScope.launch {
            val response = repository.getUserPosts()
            response.data?.let { repository.insertUserPosts(it) }
            _userPosts.value = response
            Timber.d("Data got from database: %s", response)
        }
    }
}