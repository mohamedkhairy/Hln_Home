package com.example.hlnhome.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hlnhome.database.entity.ServiceCategories


@Dao
interface Dao {

    ////////// home cache query /////////

    @Query("SELECT * from home_table")
    fun getServiceCategoriesData(): LiveData<List<ServiceCategories>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAll(data: List<ServiceCategories>)

//    @Query("UPDATE team_table SET isFavorites =:value WHERE id =:id")
//    fun updateFav(value: Boolean, id: Int)


    @Query("DELETE FROM home_table")
    fun deleteAll()

}