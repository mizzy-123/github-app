package com.dicoding.githubapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubapp.api.RetrofitClient
import com.dicoding.githubapp.api.response.DataDetail
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _detailAccount = MutableLiveData<DataDetail>()
    val detailAccount: LiveData<DataDetail> = _detailAccount

    fun getDetailAccount(username: String, token: String){
        MainScope().launch {
            try {
                _isLoading.value = true
                val retrofit = RetrofitClient.instance
                val defferedSearch = async {
                    retrofit.getDetail(token, username)
                }
                val getDataDetail = defferedSearch.await()
                _detailAccount.value = getDataDetail
                _isLoading.value = false
            } catch (e: Exception){
                Log.e("detail account", e.message.toString())
                _isLoading.value = false
            }
        }
    }
}