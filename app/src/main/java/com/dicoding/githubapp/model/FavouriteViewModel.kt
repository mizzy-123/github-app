package com.dicoding.githubapp.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubapp.database.Favourite
import com.dicoding.githubapp.repository.FavouriteRepository

class FavouriteViewModel(application: Application) : ViewModel() {

    private val mFavouriteRepository: FavouriteRepository = FavouriteRepository(application)

    fun insert(favourite: Favourite){
        mFavouriteRepository.insert(favourite)
    }

    fun update(favourite: Favourite){
        mFavouriteRepository.update(favourite)
    }

    fun delete(favourite: Favourite){
        mFavouriteRepository.delete(favourite)
    }

    fun getAllFavourite(): LiveData<List<Favourite>> = mFavouriteRepository.getAllFavourite()

    fun cekFavourite(login: String): LiveData<Int> = mFavouriteRepository.cekFavourite(login)

    fun deleteByUsername(login: String) {
        mFavouriteRepository.deleteByUsername(login)
    }
}