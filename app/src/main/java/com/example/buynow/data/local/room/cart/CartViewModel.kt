package com.example.buynow.data.local.room.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.buynow.data.local.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application){

    private val repository: CartRepo
    val allproducts: LiveData<List<ProductEntity>>
    val product: MutableLiveData<List<ProductEntity>> = MutableLiveData()

    init {
        val productDao = AppDatabase.getInstance(application).productDao()
        repository = CartRepo(productDao)
        allproducts = repository.allCartProducts
    }

    fun getProductUnPaid() = viewModelScope.launch(Dispatchers.IO) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.getProductUnPaid().observeForever { retrievedItem ->
                product.postValue(retrievedItem)
            }
        }
    }

    fun getProductUnPaidByUser(userId: String) = viewModelScope.launch(Dispatchers.IO) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.getProductUnPaidByUser(userId).observeForever { retrievedItem ->
                product.postValue(retrievedItem)
            }
        }
    }

    fun insert(product: ProductEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(product)
    }

    fun deleteCart(product: ProductEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(product)
    }

    fun updateCart(product: ProductEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(product)
    }
}