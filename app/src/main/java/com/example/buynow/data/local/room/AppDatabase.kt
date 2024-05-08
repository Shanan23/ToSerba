package com.example.buynow.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.buynow.data.local.room.cart.ProductDao
import com.example.buynow.data.local.room.cart.ProductEntity
import com.example.buynow.data.local.room.category.CategoryDao
import com.example.buynow.data.local.room.category.CategoryEntity
import com.example.buynow.data.local.room.item.ItemDao
import com.example.buynow.data.local.room.item.ItemEntity
import com.example.buynow.data.local.room.sale.SaleDao
import com.example.buynow.data.local.room.sale.SaleEntity
import java.util.concurrent.Executors

@Database(
    entities = [ProductEntity::class, ItemEntity::class, CategoryEntity::class, SaleEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase:RoomDatabase() {

    abstract fun saleDao(): SaleDao
    abstract fun categoryDao(): CategoryDao
    abstract fun itemDao(): ItemDao
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(AppDatabase::class) {
                val dbBuilder = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                dbBuilder.setQueryCallback(object : QueryCallback {
                    override fun onQuery(sqlQuery: String, bindArgs: List<Any?>) {
                        println("SQL Query: $sqlQuery SQL Args: $bindArgs")
                    }
                }, Executors.newSingleThreadExecutor())
                val instance =  dbBuilder.build()
                INSTANCE = instance
                return instance
            }
        }
    }
}