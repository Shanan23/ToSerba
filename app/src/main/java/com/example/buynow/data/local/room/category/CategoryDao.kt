package com.example.buynow.data.local.room.item

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CategoryDao {

    @Query("SELECT * FROM items order by Item_ID asc")
    fun getAll(): LiveData<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE Item_User_ID = :userId order by Item_ID asc")
    fun getByUserId(vararg userId: String): LiveData<List<ItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg item: ItemEntity)

    @Delete
    suspend fun delete(item: ItemEntity)

    @Update
    suspend fun update(vararg item: ItemEntity)
}