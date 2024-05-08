package com.example.buynow.data.local.room.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CartRepo(private val productDao: ProductDao) {

    val allCartProducts: LiveData<List<ProductEntity>> = productDao.getAll()

    val product: LiveData<ProductEntity> by lazy {
        MutableLiveData<ProductEntity>()
    }

    suspend fun getProductBySeller(sellerId: String): LiveData<List<ProductEntity>> {
        return productDao.getProductBySeller(sellerId)
    }

    suspend fun getProductUnPaidByUser(userId: String): LiveData<List<ProductEntity>> {
        return productDao.getProductUnPaidByUser(userId)
    }

    suspend fun insert(product: ProductEntity) {
        productDao.insert(product)
    }

    suspend fun delete(product: ProductEntity) {
        productDao.delete(product)
    }

    suspend fun update(product: ProductEntity) {
        productDao.update(product)
    }
}