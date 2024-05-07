package com.example.buynow.data.local.room.sale

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.buynow.data.local.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SaleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SaleRepo
    val allSales: LiveData<List<SaleEntity>>
    val sales: MutableLiveData<List<SaleEntity>> = MutableLiveData()
    val sale: MutableLiveData<SaleEntity> = MutableLiveData()

    init {
        val saleDao = AppDatabase.getInstance(application).saleDao()
        repository = SaleRepo(saleDao)
        allSales = repository.allItems
    }

    fun getBySaleID(saleId: String) = viewModelScope.launch(Dispatchers.IO) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.getBySaleId(saleId).observeForever { retrievedItem ->
                sale.postValue(retrievedItem)
            }
        }
    }

    fun getByUserID(userId: String) = viewModelScope.launch(Dispatchers.IO) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.getByUserId(userId).observeForever { retrievedItem ->
                sales.postValue(retrievedItem)
            }
        }
    }

    fun getByUserIdAndStatus(userId: String, status:String) = viewModelScope.launch(Dispatchers.IO) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.getByUserIdAndStatus(userId, status).observeForever { retrievedItem ->
                sales.postValue(retrievedItem)
            }
        }
    }

    fun insertSale(sale: SaleEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(sale)
    }

    fun deleteSale(sale: SaleEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(sale)
    }

    fun updateSale(sale: SaleEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(sale)
    }
}