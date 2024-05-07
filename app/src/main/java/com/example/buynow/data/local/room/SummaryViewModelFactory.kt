package com.example.buynow.data.local.room

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buynow.data.local.room.category.CategoryDao
import com.example.buynow.data.local.room.item.ItemDao

class SummaryViewModelFactory(
    private val context: Context,
    private val itemDao: ItemDao,
    private val categoryDao: CategoryDao,
    private val userId: String?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SummaryViewModel::class.java)) {
            return SummaryViewModel(context, itemDao, categoryDao, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}