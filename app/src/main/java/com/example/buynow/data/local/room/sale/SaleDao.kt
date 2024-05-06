package com.example.buynow.data.local.room.sale

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface SaleDao {

    @Query("SELECT * FROM sales order by Sale_ID asc")
    fun getAll(): LiveData<List<SaleEntity>>

    @Query("SELECT * FROM sales WHERE Sale_User_ID = :userId order by Sale_ID asc")
    fun getByUserId(userId: String): LiveData<List<SaleEntity>>

    @Query("SELECT * FROM sales WHERE Sale_ID = :saleId order by Sale_ID asc")
    fun getBySaleId(saleId: String): LiveData<SaleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg sale: SaleEntity)

    @Delete
    suspend fun delete(sale: SaleEntity)

    @Update
    suspend fun update(vararg sale: SaleEntity)
}