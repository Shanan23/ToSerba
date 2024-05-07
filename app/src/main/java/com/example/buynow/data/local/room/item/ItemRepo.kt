package com.example.buynow.data.local.room.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.buynow.data.local.room.sale.SaleEntity

class ItemRepo(private val itemDao: ItemDao) {

    val allItems: LiveData<List<ItemEntity>> = itemDao.getAll()

    val getByUserId: LiveData<List<ItemEntity>> = itemDao.getByUserId()

    val item: LiveData<ItemEntity> by lazy {
        MutableLiveData<ItemEntity>()
    }

    suspend fun insert(product: ItemEntity) {
        itemDao.insert(product)
    }

    suspend fun delete(product: ItemEntity) {
        itemDao.delete(product)
    }

    suspend fun update(product: ItemEntity) {
        itemDao.update(product)
    }

    suspend fun getItemById(itemId: String): LiveData<ItemEntity> {
        return itemDao.getByItemId(itemId)
    }

    suspend fun getByUserId(userId: String): LiveData<List<ItemEntity>> {
        return itemDao.getByUserId(userId)
    }
}