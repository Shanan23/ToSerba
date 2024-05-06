package com.example.buynow.data.local.room.cart

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDao {

    @Query("SELECT * FROM cart_items order by id desc")
    fun getAll(): LiveData<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg product: ProductEntity)

    @Delete
    suspend fun delete(product: ProductEntity)

    @Update
    suspend fun update(vararg product: ProductEntity)
    @Query("SELECT * FROM cart_items WHERE Product_Is_Pay = 0 order by Product_ID asc")
    fun getProductUnPaid(): LiveData<List<ProductEntity>>
    @Query("SELECT * FROM cart_items WHERE Product_Is_Pay = 0 AND Product_User_ID = :userId order by Product_ID asc")
    fun getProductUnPaidByUser(userId:String): LiveData<List<ProductEntity>>

}