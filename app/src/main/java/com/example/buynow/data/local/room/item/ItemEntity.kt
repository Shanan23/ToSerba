package com.example.buynow.data.local.room.item

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "Item_ID") var pId: Int,
    @ColumnInfo(name = "Item_User_ID") var pUId: String,
    @ColumnInfo(name = "Item_Name") var name: String,
    @ColumnInfo(name = "Item_Price") var price: Int,
    @ColumnInfo(name = "Item_Image") var image: String,
    @ColumnInfo(name = "Item_Desc") var desc: String,
    @ColumnInfo(name = "Item_Rating") var rating: Double,
    @ColumnInfo(name = "Item_Discount") var discount: String,
    @ColumnInfo(name = "Item_Stock") var stock: Int,
    @ColumnInfo(name = "Item_Fav") var fav: Boolean,
    @ColumnInfo(name = "Item_Brand") var brand: String,
    @ColumnInfo(name = "Item_Category") var category: String,
    @ColumnInfo(name = "Item_Note") var note: String,
    ) {
    var id: Int = 0
}