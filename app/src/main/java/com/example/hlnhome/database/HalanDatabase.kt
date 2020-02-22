package com.example.hlnhome.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hlnhome.converter.ListConverter
import com.example.hlnhome.database.dao.Dao
import com.example.hlnhome.database.entity.ServiceCategories


@Database(entities = [ServiceCategories::class], version = 1, exportSchema = false)
@TypeConverters(value = [(ListConverter::class)])
abstract class HalanDatabase: RoomDatabase() {

    abstract fun HalanHomeDao(): Dao


    companion object {

        private var instance: HalanDatabase? = null

        @Synchronized
        fun getInstance(context: Context): HalanDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    HalanDatabase::class.java, "halan.db"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }
}