package com.dicoding.githubapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Favourite::class], version = 1)
abstract class FavouriteRoomDatabase : RoomDatabase() {
    abstract fun favouriteDao(): FavouriteDao

    companion object {
        @Volatile
        private var INSTANCE: FavouriteRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavouriteRoomDatabase {
            if (INSTANCE == null){
                synchronized(FavouriteRoomDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavouriteRoomDatabase::class.java, "favourite_database")
                        .build()
                }
            }
            return INSTANCE as FavouriteRoomDatabase
        }
    }
}