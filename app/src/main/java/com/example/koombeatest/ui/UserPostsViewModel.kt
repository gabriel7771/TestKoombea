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
import com.example.koombeatest.utils.Status
import kotlinx.coroutines.launch
import timber.log.Timber

class UserPostsViewModel @ViewModelInject constructor(
    private val repository:UserPostsRepository
) : ViewModel() {

    private val _userPosts = MutableLiveData<Resource<UserPosts>>()
    val userPosts: LiveData<Resource<UserPosts>>
        get() = _userPosts

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    init {
        refreshUserPosts()
    }

    fun refreshUserPosts(){
        //_userPosts.value = Resource.loading(null)
        _status.value = Status.LOADING
        viewModelScope.launch {
            val response = repository.getUserPosts()
            response.data?.let {
                _status.value = Status.SUCCESS
                repository.insertUserPosts(it)
            } ?: _status.apply {
                value = Status.ERROR
            }
            _userPosts.value = response
            Timber.d("Data got from database: %s", response)
        }
    }
}