package com.dicoding.githubapp.model

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubapp.api.RetrofitClient
import com.dicoding.githubapp.api.response.DataSearch
import com.dicoding.githubapp.api.response.DataSearchItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Retrofit

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