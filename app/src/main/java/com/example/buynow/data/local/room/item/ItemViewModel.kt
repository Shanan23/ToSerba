package com.example.buynow.data.local.room.item

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.buynow.data.local.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ItemRepo
    val allItems: LiveData<List<ItemEntity>>
    val item: MutableLiveData<ItemEntity> = MutableLiveData()
    val items: MutableLiveData<List<ItemEntity>> = MutableLiveData()

    init {
        val itemDao = AppDatabase.getInstance(application).itemDao()
        repository = ItemRepo(itemDao)
        allItems = repository.allItems
    }

    fun getByItemID(itemId: String) = viewModelScope.launch(Dispatchers.IO) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.getItemById(itemId).observeForever { retrievedItem ->
                item.postValue(retrievedItem)
            }
        }
    }

    fun getByUserID(userId: String) = viewModelScope.launch(Dispatchers.IO) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.getByUserId(userId).observeForever { retrievedItem ->
                items.postValue(retrievedItem)
            }
        }
    }

    fun insertItem(item: ItemEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(item)
    }

    fun deleteItem(item: ItemEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(item)
    }

    fun updateItem(item: ItemEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(item)
    }
}