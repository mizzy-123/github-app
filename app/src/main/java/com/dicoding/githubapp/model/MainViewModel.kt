package com.dicoding.githubapp.model

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubapp.api.RetrofitClient
import com.dicoding.githubapp.api.response.DataSearch

class MainViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    @SuppressLint("ShowToast")
   suspend fun searchGithub(username: String, token: String): DataSearch{
        _isLoading.value = true
        val retrofit = RetrofitClient.instance
        val defferedSearch = retrofit.getSearch(token, username)
        _isLoading.value = false
        return defferedSearch
   }
}