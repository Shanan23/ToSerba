package com.example.buynow.data.local.room.cart

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class ProductEntity (
    @ColumnInfo(name = "Product_User_ID") var uId: String,
    @ColumnInfo(name = "Product_Name") var name: String,
    @ColumnInfo(name = "Product_Quantity") var qua: Int,
    @ColumnInfo(name = "Product_Price") var price: Int,
    @ColumnInfo(name = "Product_SubTotal") var subTotal: Int,
    @ColumnInfo(name = "Product_ID") var pId: Int,
    @ColumnInfo(name = "Product_Image") var Image: String,
    @ColumnInfo(name = "Product_Is_Check") var isCheck: Boolean,
    @ColumnInfo(name = "Product_Is_Pay") var isPay: Boolean,
    @ColumnInfo(name = "Product_Sale_ID") var saleId: String,

        ){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}