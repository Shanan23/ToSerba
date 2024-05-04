package com.example.buynow.data.local.room.item

import androidx.lifecycle.LiveData

class ItemRepo(private val itemDao: ItemDao) {

    val allItems: LiveData<List<ItemEntity>> = itemDao.getAll()

    suspend fun insert(product: ItemEntity) {
        itemDao.insert(product)
    }
    suspend fun delete(product: ItemEntity) {
        itemDao.delete(product)
    }
    suspend fun update(product: ItemEntity) {
        itemDao.update(product)
    }
}