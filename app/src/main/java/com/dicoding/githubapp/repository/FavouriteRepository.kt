package com.dicoding.githubapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.githubapp.database.Favourite
import com.dicoding.githubapp.database.FavouriteDao
import com.dicoding.githubapp.database.FavouriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavouriteRepository(application: Application) {
    private val mFavouriteDao: FavouriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavouriteRoomDatabase.getDatabase(application)
        mFavouriteDao = db.favouriteDao()
    }

    fun getAllFavourite(): LiveData<List<Favourite>> = mFavouriteDao.getAllFavourite()

    fun insert(favourite: Favourite){
        executorService.execute { mFavouriteDao.insert(favourite) }
    }

    fun delete(favourite: Favourite){
        executorService.execute { mFavouriteDao.delete(favourite) }
    }

    fun update(favourite: Favourite){
        executorService.execute { mFavouriteDao.update(favourite) }
    }

    fun cekFavourite(login: String): LiveData<Int> = mFavouriteDao.cekFavourite(login)

    fun deleteByUsername(login: String){
        executorService.execute { mFavouriteDao.deleteByUsername(login) }
    }
}