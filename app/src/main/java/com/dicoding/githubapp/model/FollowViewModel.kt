package com.dicoding.githubapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubapp.BuildConfig
import com.dicoding.githubapp.DetailActivity
import com.dicoding.githubapp.api.RetrofitClient
import com.dicoding.githubapp.api.response.DataFollowers
import com.dicoding.githubapp.api.response.DataFollowing
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FollowViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoading2 = MutableLiveData<Boolean>()
    val isLoading2: LiveData<Boolean> = _isLoading2

    private val _follower = MutableLiveData<List<DataFollowers>>()
    val follower: LiveData<List<DataFollowers>> = _follower

    private val _following = MutableLiveData<List<DataFollowing>>()
    val following: LiveData<List<DataFollowing>> = _following

    init {
        val username = DetailActivity.username
        val token: String = BuildConfig.API_KEY
        getDataFollowing(username, token)
        getDataFollower(username, token)
    }
    fun getDataFollower(username: String, token: String){
        MainScope().launch {
            try {
                _isLoading.value = true
                val retrofit = RetrofitClient.instance
                val defferedFollower = async {
                    retrofit.getFollowers(token, username)
                }
                val getFollower = defferedFollower.await()
                _follower.value = getFollower
                _isLoading.value = false
            }catch (e: Exception){
                Log.e("detail account", e.message.toString())
                _isLoading.value = false
            }

        }
    }

    fun getDataFollowing(username: String, token: String){
        MainScope().launch {
            try {
                _isLoading2.value = true
                val retrofit = RetrofitClient.instance
                val defferedFollowing = async {
                    retrofit.getFollowing(token, username)
                }
                val getFollowing = defferedFollowing.await()
                _following.value = getFollowing
                _isLoading2.value = false
            }catch (e: Exception){
                Log.e("detail account", e.message.toString())
                _isLoading2.value = false
            }
        }
    }
}