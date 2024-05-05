package com.example.buynow.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.buynow.data.local.room.card.CardDao
import com.example.buynow.data.local.room.card.CardEntity
import com.example.buynow.data.local.room.cart.ProductDao
import com.example.buynow.data.local.room.cart.ProductEntity
import com.example.buynow.data.local.room.item.ItemDao
import com.example.buynow.data.local.room.item.ItemEntity
import java.util.concurrent.Executors

@Database(entities = [ProductEntity::class, CardEntity::class, ItemEntity::class], version = 1,exportSchema = false)
abstract class AppDatabase:RoomDatabase() {

    abstract fun itemDao(): ItemDao
    abstract fun productDao(): ProductDao
    abstract fun cardDao(): CardDao

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