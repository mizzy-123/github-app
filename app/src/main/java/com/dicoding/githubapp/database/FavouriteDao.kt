package com.dicoding.githubapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favourite: Favourite)

    @Update
    fun update(favourite: Favourite)

    @Delete
    fun delete(favourite: Favourite)

    @Query("SELECT * FROM favourite ORDER BY id DESC")
    fun getAllFavourite(): LiveData<List<Favourite>>

    @Query("SELECT COUNT(*) FROM favourite WHERE login = :login")
    fun cekFavourite(login: String): LiveData<Int>

    @Query("DELETE FROM favourite WHERE login = :login")
    fun deleteByUsername(login: String)

}