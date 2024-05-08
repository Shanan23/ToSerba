package com.example.buynow.data.local.room.sale

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SaleRepo(private val saleDao: SaleDao) {

    val allItems: LiveData<List<SaleEntity>> = saleDao.getAll()

    val sale: LiveData<SaleEntity> by lazy {
        MutableLiveData<SaleEntity>()
    }

    suspend fun insert(sale: SaleEntity) {
        saleDao.insert(sale)
    }

    suspend fun delete(sale: SaleEntity) {
        saleDao.delete(sale)
    }

    suspend fun update(sale: SaleEntity) {
        saleDao.update(sale)
    }

    suspend fun getByUserId(userId: String): LiveData<List<SaleEntity>> {
        return saleDao.getByUserId(userId)
    }

    suspend fun getByUserIdAndStatus(userId: String, status: String): LiveData<List<SaleEntity>> {
        return saleDao.getByUserIdAndStatus(userId, status)
    }

    suspend fun getBySaleId(saleId: String): LiveData<SaleEntity> {
        return saleDao.getBySaleId(saleId)
    }
}