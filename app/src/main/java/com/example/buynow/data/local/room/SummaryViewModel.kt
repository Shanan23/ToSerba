package com.example.buynow.data.local.room

import android.content.Context
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.buynow.R
import com.example.buynow.data.local.room.category.CategoryDao
import com.example.buynow.data.local.room.item.ItemDao
import com.example.buynow.data.model.SummaryAdmin

class SummaryViewModel(
    context: Context,
    private val itemDao: ItemDao,
    private val categoryDao: CategoryDao,
    val userId: String?
) : ViewModel() {

    val combinedData: LiveData<List<SummaryAdmin>> = MediatorLiveData<List<SummaryAdmin>>().apply {
        if (userId == null) {
            val table1LiveData = itemDao.getAll()
            val table2LiveData = categoryDao.getAll()
            val combinedList = mutableListOf<SummaryAdmin>()

            addSource(table1LiveData) { table1Entities ->
                var bitmap =
                    BitmapFactory.decodeResource(context.resources, R.drawable.ic_admin_3)
                var summaryAdmin = SummaryAdmin(bitmap, "Daftar Produk", table1Entities.size)
                combinedList.add(summaryAdmin)
                value = combinedList
            }

            addSource(table2LiveData) { table2Entities ->
                var bitmap =
                    BitmapFactory.decodeResource(context.resources, R.drawable.ic_admin_2)
                var summaryAdmin = SummaryAdmin(bitmap, "Daftar Kategori", table2Entities.size)
                combinedList.add(summaryAdmin)
                value = combinedList
            }
        } else {
            val table1LiveData = itemDao.getByUserId(userId)
            val table2LiveData = categoryDao.getAll()
            val combinedList = mutableListOf<SummaryAdmin>()

            addSource(table1LiveData) { table1Entities ->
                var bitmap =
                    BitmapFactory.decodeResource(context.resources, R.drawable.ic_admin_3)
                var summaryAdmin = SummaryAdmin(bitmap, "Daftar Produk", table1Entities.size)
                combinedList.add(summaryAdmin)
                value = combinedList
            }

            addSource(table2LiveData) { table2Entities ->
                var bitmap =
                    BitmapFactory.decodeResource(context.resources, R.drawable.ic_admin_2)
                var summaryAdmin = SummaryAdmin(bitmap, "Daftar Kategori", table2Entities.size)
                combinedList.add(summaryAdmin)
                value = combinedList
            }
        }
    }
}