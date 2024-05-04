package com.example.buynow.data.local.room.item

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ItemDao {

    @Query("SELECT * FROM items order by id desc")
    fun getAll(): LiveData<List<ItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg item: ItemEntity)

    @Delete
    suspend fun delete(item: ItemEntity)

    @Update
    suspend fun update(vararg item: ItemEntity)
}