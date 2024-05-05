package com.example.buynow.data.local.room.category

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories order by Category_ID asc")
    fun getAll(): LiveData<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE Category_Name = :categoryName order by Category_ID asc")
    fun getByCategoryId(vararg categoryName: String): LiveData<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg item: CategoryEntity)

    @Delete
    suspend fun delete(item: CategoryEntity)

    @Update
    suspend fun update(vararg item: CategoryEntity)
}