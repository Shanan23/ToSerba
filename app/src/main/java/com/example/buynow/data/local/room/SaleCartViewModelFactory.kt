package com.example.buynow.data.local.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buynow.data.local.room.cart.ProductDao
import com.example.buynow.data.local.room.sale.SaleDao

class SaleCartViewModelFactory(
    private val productDao: ProductDao,
    private val saleDao: SaleDao,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SaleCartViewModel::class.java)) {
            return SaleCartViewModel(productDao, saleDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}