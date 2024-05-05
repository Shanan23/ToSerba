package com.example.buynow.data.local.room.category

import androidx.lifecycle.LiveData

class CategoryRepo(private val categoryDao: CategoryDao) {

    val allItems: LiveData<List<CategoryEntity>> = categoryDao.getAll()

    val getByCategoryId: LiveData<CategoryEntity> = categoryDao.getByCategoryId()

    suspend fun insert(category: CategoryEntity) {
        categoryDao.insert(category)
    }

    suspend fun delete(category: CategoryEntity) {
        categoryDao.delete(category)
    }

    suspend fun update(category: CategoryEntity) {
        categoryDao.update(category)
    }
}