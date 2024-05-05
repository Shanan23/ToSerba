package com.example.buynow.data.local.room.category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Category_ID") var pId: Int,
    @ColumnInfo(name = "Category_Name") var name: String,
    @ColumnInfo(name = "Category_Image") var image: String,
) {
    var id: Int = 0
}