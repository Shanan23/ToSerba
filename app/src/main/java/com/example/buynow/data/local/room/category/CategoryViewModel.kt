package com.example.buynow.data.local.room.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.buynow.data.local.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CategoryRepo
    val allItems: LiveData<List<CategoryEntity>>

    init {
        val categoryDao = AppDatabase.getInstance(application).categoryDao()
        repository = CategoryRepo(categoryDao)
        allItems = repository.allItems
    }

    fun insertCategory(item: CategoryEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(item)
    }

    fun deleteCategory(item: CategoryEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(item)
    }

    fun updateCategory(item: CategoryEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(item)
    }
}